package com.evolven.httpclient.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.command.CommandExceptionNotLoggedIn;
import com.evolven.common.StringUtils;
import com.evolven.common.YAMLUtils;
import com.evolven.config.ConfigException;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.httpclient.CachedURLBuilder;
import com.evolven.httpclient.EvolvenHttpClient;
import com.evolven.httpclient.http.IHttpRequestResult;
import com.evolven.logging.Logger;
import com.evolven.policy.PolicyConfig;
import com.evolven.policy.PolicyConfigFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ser.Serializers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UploadPluginCommand extends Command {

    public static final String OPTION_PLUGIN_PATH = "path";
    FileSystemManager fileSystemManager;

    Logger logger = new Logger(this);

    public static final String[] requiredFiles = {
            "plugin.yml"
    };

    public static final String[] optionalFiles = {};

    public UploadPluginCommand(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
        registerOptions(new String[] {
                OPTION_PLUGIN_PATH,
        });
    }

    @Override
    public void execute() throws CommandException {
        String pluginPath = options.get(OPTION_PLUGIN_PATH);
        testPluginDefinition(pluginPath);
        EvolvenCliConfig config = fileSystemManager.getConfig();
        try {
            config.setEnvironment();
        } catch (ConfigException e) {
            throw new CommandException("Failed to load active environment. " + e.getMessage());
        }
        String baseUrl = null;
        try {
            baseUrl = CachedURLBuilder.createBaseUrl(config);
        } catch (MalformedURLException | ConfigException e) {
            throw new CommandException("Failed to construct base URL. " + e.getMessage());
        }
        EvolvenHttpClient evolvenHttpClient = new EvolvenHttpClient(baseUrl);
        byte[] archive = null;
        try {
            archive = loadPlugin(pluginPath);
        } catch (IOException e) {
            throw new CommandException("Failed to load the plugin.", e);
        }
        uploadPlugin(evolvenHttpClient, config, Base64.getEncoder().encodeToString(archive));
    }

    private void testPluginDefinition(String pluginPath) throws CommandException {
        Path path = Paths.get(pluginPath);
        if (!Files.isDirectory(path)) {
            throw new CommandException("The path \"" + pluginPath + "\" is not a directory.");
        }
        Paths.get(pluginPath, "plugin.yml");
        try {
            Arrays.stream(requiredFiles).forEach(f -> {
                if (!Files.exists(Paths.get(pluginPath, f))) {
                    throw new RuntimeException("The required file \"" + f + "\" does not exist");
                }
            });
        } catch (RuntimeException e) {
            throw new CommandException(e);
        }
    }

    private void uploadPlugin(EvolvenHttpClient evolvenHttpClient, EvolvenCliConfig config, String base64Zip) throws CommandException {
        String apiKey = null;
        try {
            apiKey = config.getApiKey();
        } catch (ConfigException e) {
            logger.error("Could not get api key. " + e.getMessage());
            throw new CommandExceptionNotLoggedIn();
        }
        logger.debug("Api key is: " + apiKey);
        if (StringUtils.isNullOrBlank(apiKey)) {
            logger.error("Api key not found. Login is required.");
            throw new CommandExceptionNotLoggedIn();
        }

        logger.debug("Calling evolvenHttpClient.uploadPlugin...");
        IHttpRequestResult result = evolvenHttpClient.uploadPlugin(apiKey, base64Zip);
        if (result.isError()) {
            logger.debug("evolvenHttpClient.uploadPlugin returned an error...");
            String errorMsg = "Failed to upload the plugin with the cached details. Login may be required.";
            String reasonPhrase = result.getReasonPhrase();
            if (!StringUtils.isNullOrBlank(reasonPhrase)) {
                errorMsg += " " + reasonPhrase;
            }
            logger.error(errorMsg);
            throw new CommandExceptionNotLoggedIn();
        }
    }

    private void addFolderToZip(Path folder, ZipOutputStream zos) throws IOException {
        Files.walk(folder)
                .filter(path -> !Files.isDirectory(path))
                .forEach(path -> {
                    ZipEntry entry = new ZipEntry(folder.relativize(path).toString());
                    try {
                        zos.putNextEntry(entry);
                        zos.write(Files.readAllBytes(path));
                        zos.closeEntry();
                    } catch (IOException e) {
                        logger.error("Failed to load the plugin. " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                });
    }
    public byte[] loadPlugin(String path) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        try {
            addFolderToZip(Paths.get(path), zos);
        } catch (RuntimeException e) {
            logger.error("Failed to load the plugin. " + e.getMessage());
            throw new IOException(e);
        }
        zos.close();
        return baos.toByteArray();
    }

    @Override
    public String getName() {
        return "upload";
    }
}
