package com.evolven.httpclient.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.common.StringUtils;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.httpclient.CachedURLBuilder;
import com.evolven.httpclient.CachedValue;
import com.evolven.httpclient.EvolvenHttpClient;
import com.evolven.httpclient.http.HttpRequestResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.net.MalformedURLException;

public class GetPoliciesCommand extends Command {
    FileSystemManager fileSystemManager;

    public GetPoliciesCommand(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
    }

    private String createBaseUrl(EvolvenCliConfig config) throws CommandException {
        CachedURLBuilder builder = new CachedURLBuilder(config);
        try {
            return builder.build();
        } catch (MalformedURLException e) {
            throw new CommandException("Failed to construct base URL. " + e.getMessage());
        }
    }

    @Override
    public void execute() throws CommandException {
        EvolvenCliConfig config = fileSystemManager.getEvolvenCliConfig();
        CachedValue cachedValue = new CachedValue(options);
        String baseUrl = createBaseUrl(config);
        EvolvenHttpClient evolvenHttpClient = new EvolvenHttpClient(baseUrl);
        getPolicies(evolvenHttpClient, config);
    }

    private void getPolicies(EvolvenHttpClient evolvenHttpClient, EvolvenCliConfig config) throws CommandException {
        String apiKey = config.getApiKey();
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
        try {
            System.out.println(asYaml(result.getContent()));;
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
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
