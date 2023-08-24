package com.evolven.httpclient.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.command.CommandExceptionNotLoggedIn;
import com.evolven.common.StringUtils;
import com.evolven.config.ConfigException;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.httpclient.CachedURLBuilder;
import com.evolven.httpclient.EvolvenHttpClient;
import com.evolven.httpclient.http.IHttpRequestResult;
import com.evolven.httpclient.model.AgentStatus;
import com.evolven.httpclient.response.AgentStatusResponse;
import com.evolven.logging.LoggerManager;

import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetAgentStatusCommand extends Command {

    public static final String OPTION_AGENT_HOST_ID = "host";
    FileSystemManager fileSystemManager;

    Logger logger = LoggerManager.getLogger(this);

    public static final String[] requiredFiles = {
            "plugin.yml"
    };

    public static final String[] optionalFiles = {};

    public GetAgentStatusCommand(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
        registerOptions(new String[] {
                OPTION_AGENT_HOST_ID
        });
    }

    @Override
    public void execute() throws CommandException {
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
        getAgentStatus(evolvenHttpClient, config);
    }


    private void getAgentStatus(EvolvenHttpClient evolvenHttpClient, EvolvenCliConfig config) throws CommandException {
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
        if (StringUtils.isNullOrBlank(hostId)) {
            String msg = "Host ID is required";
            logger.log(Level.SEVERE, msg);
            throw new CommandException(msg);
        }
        logger.fine("Calling evolvenHttpClient.upgradeAgent...");
        IHttpRequestResult result = evolvenHttpClient.getAgentStatus(apiKey, hostId);
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
        Iterator<AgentStatus> agentStatusResponse = new AgentStatusResponse(result.getContent()).iterator();
        if (agentStatusResponse.hasNext()) {
            String format = "%-20s    | %s%n";
            System.out.printf(format, "Status", "Version");
            System.out.printf("-------------------------------------------\n");
            AgentStatus agentStatus = agentStatusResponse.next();
            System.out.printf(format, agentStatus.getStatus(), agentStatus.getVersion());
        }
    }

    @Override
    public String getName() {
        return "upgrade";
    }
}
