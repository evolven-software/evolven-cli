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

public class UpgradeAgentCommand extends CommandEnv {

    private final Logger logger = LoggerManager.getLogger(this);

    public static final String[] requiredFiles = {
            "plugin.yml"
    };

    public static final String[] optionalFiles = {};

    public UpgradeAgentCommand(FileSystemManager fileSystemManager) {
        super(fileSystemManager);

        registerOptions(
                OPTION_AGENT_HOST_ID,
		        OPTION_AGENT_VERSION
        );
    }

    @Override
    public void execute() throws CommandException {
        EvolvenCliConfig config = getConfig();
        setEnvironmentFromConfig(config);
        String baseUrl = null;
        try {
            baseUrl = CachedURLBuilder.createBaseUrl(config);
        } catch (MalformedURLException | ConfigException e) {
            throw new CommandException("Failed to construct base URL.", e);
        }
        EvolvenHttpClient evolvenHttpClient = new EvolvenHttpClient(baseUrl);
        upgradeAgent(evolvenHttpClient, config);
    }


    private void upgradeAgent(EvolvenHttpClient evolvenHttpClient, EvolvenCliConfig config) throws CommandException {
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
        String hostId = options.get(OPTION_AGENT_HOST_ID);
        String version = options.get(OPTION_AGENT_VERSION);
        if (StringUtils.isNullOrBlank(hostId)) {
            String msg = "Host ID is required";
            logger.log(Level.SEVERE, msg);
            throw new CommandException(msg);
        }
        if (StringUtils.isNullOrBlank(version)) {
            String msg = "New agent version is required";
            logger.log(Level.SEVERE, msg);
            throw new CommandException(msg);
        }
        logger.fine("Calling evolvenHttpClient.upgradeAgent...");
        IHttpRequestResult result = evolvenHttpClient.upgradeAgent(apiKey, hostId, version);
        if (result.isError()) {
            logger.fine("evolvenHttpClient.upgradeAgent returned an error...");
            String errorMsg = "Failed to upgrade the agent (login may be required)";
            String reasonPhrase = result.getReasonPhrase();
            if (!StringUtils.isNullOrBlank(reasonPhrase)) {
                errorMsg += "\nReason phrase: " + reasonPhrase;
            }
            logger.log(Level.SEVERE, errorMsg);
            throw new CommandException(errorMsg);
        }
    }

    @Override
    public String getName() {
        return "upgrade";
    }
}
