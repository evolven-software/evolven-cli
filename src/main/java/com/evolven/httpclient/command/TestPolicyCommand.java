package com.evolven.httpclient.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.common.StringUtils;
import com.evolven.common.YAMLUtils;
import com.evolven.config.ConfigException;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.httpclient.CachedURLBuilder;
import com.evolven.httpclient.EvolvenHttpClient;
import com.evolven.httpclient.http.IHttpRequestResult;
import com.evolven.httpclient.response.EnvironmentsResponse;
import com.evolven.httpclient.model.Environment;
import com.evolven.policy.PolicyConfig;
import com.evolven.policy.PolicyConfigFactory;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class TestPolicyCommand extends Command {

    public static final String OPTION_POLICY_FILENAME = "filename";
    public static final String OPTION_QUERY = "query";
    FileSystemManager fileSystemManager;

    public TestPolicyCommand(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
        registerOptions(new String[] {
                OPTION_POLICY_FILENAME,
                OPTION_QUERY,
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
        Map<String, String> policies = null;
        try {
            policies = fromYamlFile(options.get(OPTION_POLICY_FILENAME));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        testPolicy(evolvenHttpClient, config,  policies, options.get(OPTION_QUERY));
    }

    private void testPolicy(EvolvenHttpClient evolvenHttpClient, EvolvenCliConfig config, Map<String, String> policies, String query) throws CommandException {
        String apiKey = null;
        try {
            apiKey = config.getApiKey();
        } catch (ConfigException e) {
            throw new CommandException("Could not get api key. " + e.getMessage());
        }
        if (StringUtils.isNullOrBlank(apiKey)) {
            throw new CommandException("Api key not found. Login is required.");
        }

        IHttpRequestResult result = evolvenHttpClient.search(apiKey, query);
        if (result.isError()) {
            String errorMsg = "Failed to get policies with the cached details. Login may be required.";
            String reasonPhrase = result.getReasonPhrase();
            if (!StringUtils.isNullOrBlank(reasonPhrase)) {
                errorMsg += " " + reasonPhrase;
            }
            throw new CommandException(errorMsg);
        }
        EnvironmentsResponse response = new EnvironmentsResponse(result.getContent());
        Iterator<Environment> envIterator = response.iterator();

        String format = "%-30s| %-30s| %s";
        boolean headerLine = false;

        while (envIterator.hasNext()) {
            Environment env = envIterator.next();
            result = evolvenHttpClient.testPolicy(apiKey, policies, env.getEnvId());
            if (result.isError()) {
                String errorMsg = "Failed to run benchmark for the environment with the name \"" + env.getName() + "\"." ;
                String reasonPhrase = result.getReasonPhrase();
                if (!StringUtils.isNullOrBlank(reasonPhrase)) {
                    errorMsg += " " + reasonPhrase;
                }
                throw new CommandException(errorMsg);
            }
            Iterator<Environment> benchmarkResultIterator = new EnvironmentsResponse(result.getContent()).iterator();
            if (benchmarkResultIterator.hasNext()) {
                if (!headerLine) {
                    System.out.println(String.format(format, "Name:", "EnvID:", "Result:"));
                    headerLine = true;
                }
                System.out.println(String.format(format,
                        env.getName(),
                        env.getEnvId(),
                        benchmarkResultIterator.next().isCompliance() ? "PASSED" : "FAILED"));
            }
        }

    }

    public Map<String, String> fromYamlFile(String filepath) throws IOException {
        File policyYamlFile = new File(filepath);
        PolicyConfig policyConfig = PolicyConfigFactory.createConfig(fileSystemManager.getPolicyConfigFile());
        JsonNode rule = YAMLUtils.load(policyYamlFile);
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
