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
import com.evolven.httpclient.EvolvenHttpRequestFilter;
import com.evolven.httpclient.http.IHttpRequestResult;
import com.evolven.httpclient.response.PullPolicyResponse;
import com.evolven.logging.LoggerManager;
import com.evolven.policy.Policy;
import com.evolven.policy.PolicyConfigFactory;
import com.evolven.policy.PolicyWriter;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public class PullPolicyCommand extends CommandEnv {

    public static final String FLAG_FORCE = "force";
    public static final String FLAG_COMMENT = "comment";
    public static final String FLAG_ALL = "all";

    private final Logger logger = LoggerManager.getLogger(this);

    public PullPolicyCommand(FileSystemManager fileSystemManager) {
        super(fileSystemManager);

        registerOptions(
                OPTION_OUTPUT,
		        OPTION_FILENAME,
		        OPTION_FORMAT,
		        OPTION_POLICY_NAME
        );

        registerFlags(
                FLAG_FORCE,
		        FLAG_COMMENT,
		        FLAG_ALL
        );
    }
    
    @Override
    public void execute() throws CommandException {
        EvolvenCliConfig config = getConfig();
        setEnvironmentFromConfig(config);
        String baseUrl = null;
        try {
            baseUrl = createBaseUrl(config);
        } catch (ConfigException e) {
            throw new CommandException("Failed to construct server base url.", e);
        }
        EvolvenHttpClient evolvenHttpClient = new EvolvenHttpClient(baseUrl);
        getPolicies(evolvenHttpClient, config);
    }

    private String createBaseUrl(EvolvenCliConfig config) throws CommandException, ConfigException {
        CachedURLBuilder builder = new CachedURLBuilder(config);
        try {
            return builder.build();
        } catch (MalformedURLException e) {
            throw new CommandException("Failed to construct base URL.", e);
        }
    }

    private void getPolicies(EvolvenHttpClient evolvenHttpClient, EvolvenCliConfig config) throws CommandException {
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
        EvolvenHttpRequestFilter evolvenHttpRequestFilter = new EvolvenHttpRequestFilter();
        if (!StringUtils.isNullOrBlank(options.get(OPTION_POLICY_NAME))) {
            evolvenHttpRequestFilter.add("name", options.get(OPTION_POLICY_NAME));
        }
        IHttpRequestResult result = evolvenHttpClient.getPolicies(apiKey, evolvenHttpRequestFilter);
        if (result.isError()) {
            String errorMsg = "Failed to get policies with the cached details. Login may be required.";
            String reasonPhrase = result.getReasonPhrase();
            if (!StringUtils.isNullOrBlank(reasonPhrase)) {
                errorMsg += " " + reasonPhrase;
            }
            logger.fine(errorMsg);
            throw new CommandExceptionNotLoggedIn();

        }
        try {
            writePolicy(result.getContent());
        } catch (JsonProcessingException e) {
            throw new CommandException("Failed to parse policy.", e);
        } catch (IOException e) {
            throw new CommandException("Failed to save policy to the local storage.", e);
        }
    }

    public void writePolicy(String jsonString) throws IOException, CommandException {
        File outputDirectory = new File(options.get(OPTION_OUTPUT));
        if (outputDirectory.exists()) {
            if (!outputDirectory.isDirectory()) {
                throw new CommandException("Invalid output location (the location exits and it is not a directory): "
                       + outputDirectory.toPath());
            }
            if (!flags.get(FLAG_FORCE)) {
                throw new CommandException("The output location (" + outputDirectory + ") exists already.");
            }
        }
        Files.createDirectories(outputDirectory.toPath());
        PullPolicyResponse pullPolicyResponse = new PullPolicyResponse(jsonString);
        Iterator<Policy> iterator = pullPolicyResponse.iterator();
        PolicyConfigFactory policyConfigFactory = new PolicyConfigFactory(fileSystemManager.getPolicyConfigFile());
        PolicyWriter policyWriter = new PolicyWriter(policyConfigFactory.createConfig());
        IndexedCache indexedCache = new IndexedCache();
        while (iterator.hasNext()) {
            Policy policy = iterator.next();
            String fileName = indexedCache.get(StringUtils.replaceNonPathCompatibleChars(policy.getName())) + ".yaml";
            File policyFile = new File(outputDirectory, fileName);
            policyWriter.write(policyFile, policy);
        }
    }

    private static class IndexedCache {
        private final Map<String, Integer> cache = new HashMap<>();

        public String get(String s) {
            Integer ix = cache.get(s);
            if (ix == null) {
                cache.put(s, 0);
            } else {
                cache.put(s, ix + 1);
                s += "__" + ix;
            }
            return s;
        }
    }

    @Override
    public String getName() {
        return "pull";
    }
}
