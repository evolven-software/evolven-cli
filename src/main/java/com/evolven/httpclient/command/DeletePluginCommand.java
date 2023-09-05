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

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeletePluginCommand extends CommandEnv {

    private final Logger logger = LoggerManager.getLogger(this);

    public static final String[] requiredFiles = {
            "plugin.yml"
    };

    public static final String[] optionalFiles = {};

    public DeletePluginCommand(FileSystemManager fileSystemManager) {
        super(fileSystemManager);

        registerOptions(
                OPTION_PLUGIN_ID
        );
    }

    @Override
    public void execute() throws CommandException {
        String pluginId = options.get(OPTION_PLUGIN_ID);
        long id;
        try {
            id = Long.parseLong(pluginId);
        } catch (NumberFormatException e) {
            throw new CommandException("Invalid id: " + pluginId, e);
        }
        EvolvenCliConfig config = getConfig();
        setEnvironmentFromConfig(config);
        String baseUrl = null;
        try {
            baseUrl = CachedURLBuilder.createBaseUrl(config);
        } catch (MalformedURLException | ConfigException e) {
            throw new CommandException("Failed to construct base URL.", e);
        }
        EvolvenHttpClient evolvenHttpClient = new EvolvenHttpClient(baseUrl);
        deletePlugin(evolvenHttpClient, config, id);
    }

    private void deletePlugin(EvolvenHttpClient evolvenHttpClient, EvolvenCliConfig config, Long pluginId) throws CommandException {
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
        IHttpRequestResult result = evolvenHttpClient.deletePlugin(apiKey, pluginId);
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

    @Override
    public String getName() {
        return "delete";
    }
}
