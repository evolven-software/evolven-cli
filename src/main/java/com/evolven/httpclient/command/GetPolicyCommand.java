package com.evolven.httpclient.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.common.StringUtils;
import com.evolven.config.ConfigException;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.httpclient.CachedURLBuilder;
import com.evolven.httpclient.EvolvenHttpClient;
import com.evolven.httpclient.http.HttpRequestResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;

public class GetPolicyCommand extends Command {
    FileSystemManager fileSystemManager;
    public static final String OPTION_OUTPUT = "output";
    public static final String OPTION_SINGLE_FILENAME = "filename";
    public static final String OPTION_FORMAT = "format";
    public static final String FLAG_FORCE = "force";

    public GetPolicyCommand(FileSystemManager fileSystemManager) {
        registerOptions(new String[] {
                OPTION_OUTPUT,
                OPTION_SINGLE_FILENAME,
                OPTION_FORMAT,
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
        HttpRequestResult result = evolvenHttpClient.getPolicies(apiKey);
        if (result.isError()) {
            String errorMsg = "Failed to get policies with the cached details. Login may be required.";
            String reasonPhrase = result.getReasonPhrase();
            if (!StringUtils.isNullOrBlank(reasonPhrase)) {
                errorMsg += " " + reasonPhrase;
            }
            throw new CommandException(errorMsg);
        }
        //System.out.println(result.getContent());;
        try {
            toYamlFiles(result.getContent());
        } catch (JsonProcessingException e) {
            System.out.println("ERROR");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("ERROR");
            System.out.println(e.getMessage());
        }
    }

    public void toYamlFiles(String jsonString) throws IOException {
        File outputDirectory = new File(options.get(OPTION_OUTPUT));
        Files.createDirectories(outputDirectory.toPath());
        JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString);
        JsonNode rules = jsonNodeTree.get("Next").get("Rule");
        for (JsonNode rule : rules) {
            String policyName = rule.get("Name").asText().replaceAll("[^a-zA-Z0-9\\.\\-]", "_"); ;
            //System.out.println(jsonAsYaml);
            //System.out.println("Policy name: " + policyName);
            //System.out.println("------------------------------------------------------------");

            YAMLMapper mapper = new YAMLMapper(YAMLFactory.builder()
                    .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                    .build());

            String yamlString = mapper.writeValueAsString(rule);
            Files.write(new File(outputDirectory, policyName + ".yaml").toPath(), yamlString.getBytes());

            //YAMLMapper yamlMapper = new YAMLMapper();
            //yamlMapper.writeValue(System.out, rule);
            //System.out.println("------------------------------------------------------------");


            //System.out.println(yamlMapper.writeValueAsString(rule));
        }

    }
    public String asYaml(String jsonString) throws JsonProcessingException  {
        JsonNode jsonNodeTree = new ObjectMapper().readTree(jsonString);
        String jsonAsYaml = new YAMLMapper().writeValueAsString(jsonNodeTree);
        return jsonAsYaml;
    }

    @Override
    public String getName() {
        return "get-policies";
    }
}
