package com.evolven.httpclient.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.common.StringUtils;
import com.evolven.config.ConfigException;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.httpclient.CachedURLBuilder;
import com.evolven.httpclient.EvolvenHttpClient;
import com.evolven.httpclient.EvolvenHttpRequestFilter;
import com.evolven.httpclient.http.IHttpRequestResult;
import com.evolven.httpclient.response.PullPolicyResponse;
import com.evolven.policy.Policy;
import com.evolven.policy.PolicyConfigFactory;
import com.evolven.policy.PolicyWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.IntStream;

public class PullPolicyCommand extends Command {
    FileSystemManager fileSystemManager;
    public static final String OPTION_OUTPUT = "output";
    public static final String OPTION_SINGLE_FILENAME = "filename";
    public static final String OPTION_FORMAT = "format";
    public static final String OPTION_POLICY_NAME = "name";
    public static final String FLAG_FORCE = "force";

    public PullPolicyCommand(FileSystemManager fileSystemManager) {
        registerOptions(new String[] {
                OPTION_OUTPUT,
                OPTION_SINGLE_FILENAME,
                OPTION_FORMAT,
                OPTION_POLICY_NAME,
        });
        registerFlag(FLAG_FORCE);
        this.fileSystemManager = fileSystemManager;
    }

    private String createBaseUrl(EvolvenCliConfig config) throws CommandException, ConfigException {
        CachedURLBuilder builder = new CachedURLBuilder(config);
        try {
            return builder.build();
        } catch (MalformedURLException e) {
            throw new CommandException("Failed to construct base URL. " + e.getMessage());
        }
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
            baseUrl = createBaseUrl(config);
        } catch (ConfigException e) {
            throw new CommandException("Failed to construct server base url. " + e.getMessage());
        }
        EvolvenHttpClient evolvenHttpClient = new EvolvenHttpClient(baseUrl);
        getPolicies(evolvenHttpClient, config);
    }

    private void getPolicies(EvolvenHttpClient evolvenHttpClient, EvolvenCliConfig config) throws CommandException {
        String apiKey = null;
        try {
            apiKey = config.getApiKey();
        } catch (ConfigException e) {
            throw new CommandException("Could not get api key. " + e.getMessage());
        }
        if (StringUtils.isNullOrBlank(apiKey)) {
            throw new CommandException("Api key not found. Login is required.");
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
            throw new CommandException(errorMsg);
        }
        try {
            writePolicy(result.getContent());
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void writePolicy(String jsonString) throws IOException, CommandException {
        File outputDirectory = new File(options.get(OPTION_OUTPUT));
        if (outputDirectory.exists()) {
            if (!outputDirectory.isDirectory()) {
                throw new CommandException("Invalid output location (the location exits and it is not a directory): "
                       + outputDirectory.toPath());
            }
            if (flags.get(FLAG_FORCE)) {
                FileUtils.deleteDirectory(outputDirectory);
            }
        }
        Files.createDirectories(outputDirectory.toPath());
        PullPolicyResponse pullPolicyResponse = new PullPolicyResponse(jsonString);
        Iterator<Policy> iterator = pullPolicyResponse.iterator();
        PolicyConfigFactory policyConfigFactory = new PolicyConfigFactory(fileSystemManager.getPolicyConfigFile());
        PolicyWriter policyWriter = new PolicyWriter(policyConfigFactory.createConfig());
        while (iterator.hasNext()) {
            Policy policy = iterator.next();
            String policyName =StringUtils.replaceNonPathCompatibleChars(policy.getName());
            String fileName = policyName + ".yaml";
            File policyFile = new File(outputDirectory, fileName);
            if (policyFile.exists()) {
                for (int ix = 0; ix < 256; ix++) {
                    fileName = policyName + "__" + ix + ".yaml";
                    policyFile = new File(outputDirectory, fileName);
                    if (!policyFile.exists()) break;
                }
            }
            policyWriter.write(policyFile, policy);
        }
    }

    @Override
    public String getName() {
        return "pull";
    }
}
