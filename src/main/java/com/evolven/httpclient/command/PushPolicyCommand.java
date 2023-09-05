package com.evolven.httpclient.command;

import com.evolven.command.CommandEnv;
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
import com.evolven.logging.LoggerManager;
import com.evolven.policy.PolicyConfig;
import com.evolven.policy.PolicyConfigFactory;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PushPolicyCommand extends CommandEnv {

    private final Logger logger = LoggerManager.getLogger(this);

    public PushPolicyCommand(FileSystemManager fileSystemManager) {
        super(fileSystemManager);

        registerOptions(
                OPTION_POLICY_FILENAME
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
        Map<String, String> policies = null;
        try {
            policies = fromYamlFile(options.get(OPTION_POLICY_FILENAME));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        pushPolicy(evolvenHttpClient, config,  policies);
    }

    private void pushPolicy(EvolvenHttpClient evolvenHttpClient, EvolvenCliConfig config, Map<String, String> policies) throws CommandException {
        String apiKey = null;
        try {
            apiKey = config.getApiKey();
        } catch (ConfigException e) {
            logger.log(Level.SEVERE, "Could not get api key. " + e.getMessage());
            throw new CommandExceptionNotLoggedIn();
        }
        if (StringUtils.isNullOrBlank(apiKey)) {
            logger.log(Level.SEVERE, "Api key not found. Login is required.");
            throw new CommandExceptionNotLoggedIn();
        }
        IHttpRequestResult result = evolvenHttpClient.pushPolicy(apiKey, policies);
        if (result.isError()) {
            String errorMsg = "Failed to get policies with the cached details. Login may be required.";
            String reasonPhrase = result.getReasonPhrase();
            if (!StringUtils.isNullOrBlank(reasonPhrase)) {
                errorMsg += " " + reasonPhrase;
            }
            logger.log(Level.SEVERE, errorMsg);
            throw new CommandExceptionNotLoggedIn();
        }
    }

    public Map<String, String> fromYamlFile(String filepath) throws IOException {
        File policyYamlFile = new File(filepath);

        PolicyConfig policyConfig = PolicyConfigFactory.createConfig(fileSystemManager.getPolicyConfigFile());
        JsonNode rule = YAMLUtils.load(policyYamlFile);
        if (rule == null) throw new IOException("Couldn't load policy config file.");
        return policyConfig.getEditablePolicyFields()
                .stream()
                .filter(f -> rule.get(f) != null)
                .collect(Collectors.toMap(f -> f, f -> rule.get(f).asText()));
    }

    @Override
    public String getName() {
        return "push";
    }
}
