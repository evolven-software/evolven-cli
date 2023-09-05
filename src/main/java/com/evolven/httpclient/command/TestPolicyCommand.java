package com.evolven.httpclient.command;

import com.evolven.command.*;
import com.evolven.common.Spinner;
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
import com.evolven.logging.LoggerManager;
import com.evolven.policy.PolicyConfig;
import com.evolven.policy.PolicyConfigFactory;
import com.evolven.policy.PolicyConfigDefault;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TestPolicyCommand extends CommandEnv {

    public static final String FLAG_USE_POLICY_SCOPE = "scope";

    private final Logger logger = LoggerManager.getLogger(this);

    public TestPolicyCommand(FileSystemManager fileSystemManager) {
        super(fileSystemManager);

        registerOptions(
                OPTION_FILENAME,
                OPTION_QUERY
        );

        registerFlags(
                FLAG_USE_POLICY_SCOPE
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
            policies = fromYamlFile(options.get(OPTION_FILENAME));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ParametersProcessor parametersProcessor = new ParametersProcessor(policies, options.get(OPTION_QUERY));
        testPolicy(evolvenHttpClient, config,  parametersProcessor.getPolicies(), parametersProcessor.getQuery());
    }

    private void testPolicy(EvolvenHttpClient evolvenHttpClient, EvolvenCliConfig config, Map<String, String> policies, String query) throws CommandException {
        String apiKey = null;
        try {
            apiKey = config.getApiKey();
        } catch (ConfigException e) {
            logger.fine("Could not get api key. " + e.getMessage());
            throw new CommandExceptionNotLoggedIn();
        }
        if (StringUtils.isNullOrBlank(apiKey)) {
            logger.fine("Api key not found. Login is required.");
            throw new CommandExceptionNotLoggedIn();
        }
        Iterator<Environment> envIterator = SearchCommand.search(evolvenHttpClient, apiKey, query);
        PolicyTestResultAccumulator policyTestResultAccumulator = new PolicyTestResultAccumulator();
        Spinner spinner = new Spinner();
        try {
            while (envIterator.hasNext()) {
                spinner.start();
                Environment env = envIterator.next();
                IHttpRequestResult result = evolvenHttpClient.testPolicy(apiKey, policies, env.getEnvId());
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
        } finally {
            spinner.stop();
        }
        policyTestResultAccumulator.print(System.out);
        if (policyTestResultAccumulator.numFailed > 0) {
            throw new CommandFailure();
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


    class ParametersProcessor {
        private final Map<String, String> policies;
        private final String query;
        public ParametersProcessor(Map<String, String> policies, String query) {
            this.policies = policies;
            this.query = query == null ? "" : query;
        }

        private List<String> toList(String... queries) {
            return Arrays.stream(queries).filter(q -> !StringUtils.isNullOrBlank(q)).collect(Collectors.toList());
        }

        public String getQuery() {
            List<String> queries = toList(
                    "ad=" + policies.get(PolicyConfigDefault.ENVIRONMENT_TYPE_FIELD),
                    flags.get(FLAG_USE_POLICY_SCOPE) ? policies.get(PolicyConfigDefault.ENVIRONMENT_NAME_FIELD) : "",
                    query);
            if (queries.size() == 1) return queries.get(0);
            return queries.stream().map(q -> "(" + q + ")").collect(Collectors.joining(" AND "));
        }

        public Map<String, String> getPolicies() {
            if (!flags.get(FLAG_USE_POLICY_SCOPE)) {
                policies.remove(PolicyConfigDefault.ENVIRONMENT_NAME_FIELD);
            }
            return policies;
        }
    }

    private static class PolicyTestResultAccumulator {

        private static class PolicyTestResult {
            String envName;
            String envId;
            String value;
            boolean testResult;
            public PolicyTestResult(String envName, String envId, boolean testResult, String value) {
                this.envName = envName;
                this.envId = envId;
                this.testResult = testResult;
                this.value = value;
            }

            public String toLine(String format) {
                String line = String.format(format,
                        envName,
                        envId,
                        testResult ? "PASSED" : "FAILED",
                        value);
                return envName + line.substring(envName.length()).replace(' ', '.');
            }
        }

        String format = "%-35s|%-35s|%-8s|%s";
        String header = String.format(format, "Host", "Environment:", "Result:", "Value:");
        String separator = StringUtils.repeat("-", header.length() + 5);
        int numFailed = 0;
        List<PolicyTestResult> result = new ArrayList<>();

        public void add(Environment env) {
            numFailed += env.isCompliance() ? 0 : 1;
            result.add(new PolicyTestResult(env.getHost(), env.getName(), env.isCompliance(), env.getValue()));
        }

        void print(PrintStream out) {
            if (result.isEmpty()) {
                out.println("Number of found environments: 0");
                return;
            }
            out.println(separator);
            out.println(header);
            out.println(separator);
            result.forEach(r -> out.println(r.toLine(format)));
            out.println(separator);
            out.println(String.format("Number of found environments: %d; %d passed; %d failed", result.size(), result.size() - numFailed, numFailed));
            if (numFailed == 0) out.println("SUCCESS");
            else out.println("FAILED");

        }
    }
    @Override
    public String getName() {
        return "push";
    }

}
