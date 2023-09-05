package com.evolven.httpclient.command;

import com.evolven.command.CommandEnv;
import com.evolven.command.CommandException;
import com.evolven.command.CommandExceptionNotLoggedIn;
import com.evolven.common.StringUtils;
import com.evolven.config.ConfigException;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.httpclient.CachedURLBuilder;
import com.evolven.httpclient.EvolvenHttpClient;
import com.evolven.httpclient.http.IHttpRequestResult;
import com.evolven.logging.LoggerManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UploadPluginCommand extends CommandEnv {

    private final Logger logger = LoggerManager.getLogger(this);

    public static final String[] requiredFiles = {
            "plugin.yml"
    };

    public static final String[] optionalFiles = {};

    public UploadPluginCommand(FileSystemManager fileSystemManager) {
        super(fileSystemManager);

        registerOptions(
                OPTION_PLUGIN_PATH
        );
    }

    @Override
    public void execute() throws CommandException {
        EvolvenCliConfig config = getConfig();
        setEnvironmentFromConfig(config);
        String pluginPath = options.get(OPTION_PLUGIN_PATH);
        testPluginDefinition(pluginPath);
        String baseUrl = null;
        try {
            baseUrl = CachedURLBuilder.createBaseUrl(config);
        } catch (MalformedURLException | ConfigException e) {
            throw new CommandException("Failed to construct base URL.", e);
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
            logger.log(Level.SEVERE, "Could not get api key. ", e);
            throw new CommandExceptionNotLoggedIn();
        }
        logger.fine("Api key is: " + apiKey);
        if (StringUtils.isNullOrBlank(apiKey)) {
            logger.log(Level.SEVERE, "Api key not found. Login is required.");
            throw new CommandExceptionNotLoggedIn();
        }

        logger.fine("Calling evolvenHttpClient.uploadPlugin...");
        IHttpRequestResult result = evolvenHttpClient.uploadPlugin(apiKey, base64Zip);
        if (result.isError()) {
            logger.fine("evolvenHttpClient.uploadPlugin returned an error...");
            String errorMsg = "Failed to upload the plugin with the cached details. Login may be required.";
            String reasonPhrase = result.getReasonPhrase();
            if (!StringUtils.isNullOrBlank(reasonPhrase)) {
                errorMsg += "\nReason phrase: " + reasonPhrase;
            }
            logger.log(Level.SEVERE, errorMsg);
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
                        logger.log(Level.SEVERE, "Failed to load the plugin.", e);
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
            logger.log(Level.SEVERE, "Failed to load the plugin. ", e);
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
