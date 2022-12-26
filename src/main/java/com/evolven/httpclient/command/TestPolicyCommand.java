package com.evolven.httpclient.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.command.CommandExceptionLogin;
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
import com.evolven.logging.Logger;
import com.evolven.policy.PolicyConfig;
import com.evolven.policy.PolicyConfigFactory;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestPolicyCommand extends Command {

    public static final String OPTION_POLICY_FILENAME = "filename";
    public static final String OPTION_QUERY = "query";
    FileSystemManager fileSystemManager;
    Logger logger = new Logger(this);

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

    private boolean testPolicy(EvolvenHttpClient evolvenHttpClient, String apiKey, Map<String, String> policies, String envId, boolean printHeader) throws CommandException {
            IHttpRequestResult result = evolvenHttpClient.testPolicy(apiKey, policies, envId);
            if (result.isError()) {
                String errorMsg = "Failed to run benchmark for the environment with the id: \"" + envId + "\"." ;
                String reasonPhrase = result.getReasonPhrase();
                if (!StringUtils.isNullOrBlank(reasonPhrase)) {
                    errorMsg += " " + reasonPhrase;
                }
                throw new CommandException(errorMsg);
            }
            Iterator<Environment> benchmarkResultIterator = new EnvironmentsResponse(result.getContent()).iterator();
            PolicyTestResultAccumulator policyTestResultAccumulator = new PolicyTestResultAccumulator();
            if (benchmarkResultIterator.hasNext()) {
                policyTestResultAccumulator.add(benchmarkResultIterator.next());
            }
            return printHeader;
    }


    class PolicyTestResultAccumulator {

        class PolicyTestResult {
            String envName;
            String envId;
            boolean testResult;
            public PolicyTestResult(String envName, String envId, boolean testResult) {
                this.envName = envName;
                this.envId = envId;
                this.testResult = testResult;
            }

            public String toLine(String format) {
                String line = String.format(format,
                        envName,
                        envId,
                        testResult ? "PASSED" : "FAILED");
                return envName + line.substring(envName.length()).replace(' ', '.');
            }
        }

        String format = "%-60s|%-10s|%s";
        String header = String.format(format, "Name:", "EnvID:", "Result:");
        String separator = StringUtils.repeat("-", header.length() + 5);
        int numFailed = 0;
        List<PolicyTestResult> result = new ArrayList<>();

        public void add(Environment env) {
            numFailed += env.isCompliance() ? 0 : 1;
            result.add(new PolicyTestResult(env.getName(), env.getEnvId(), env.isCompliance()));
        }

        void print(PrintStream out) {
            if (result.size() == 0) return;
            out.println(separator);
            out.println(header);
            out.println(separator);
            result.stream().forEach(r -> out.println(r.toLine(format)));
            out.println(separator);
            out.println(String.format("Total: %d; %d passed; %d failed", result.size(), result.size() - numFailed, numFailed));
            if (numFailed == 0) out.println("SUCCESS");
            else out.println("FAILED");

        }


    }
    private void testPolicy(EvolvenHttpClient evolvenHttpClient, EvolvenCliConfig config, Map<String, String> policies, String query) throws CommandException {
        String apiKey = null;
        try {
            apiKey = config.getApiKey();
        } catch (ConfigException e) {
            logger.error("Could not get api key. " + e.getMessage());
            throw new CommandExceptionLogin();
        }
        if (StringUtils.isNullOrBlank(apiKey)) {
            logger.error("Api key not found. Login is required.");
            throw new CommandExceptionLogin();
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
        PolicyTestResultAccumulator policyTestResultAccumulator = new PolicyTestResultAccumulator();
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
                policyTestResultAccumulator.add(benchmarkResultIterator.next());
            }
        }
        policyTestResultAccumulator.print(System.out);
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
