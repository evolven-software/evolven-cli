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
import com.evolven.logging.LoggerManager;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListPluginsCommand extends Command {
    private static final int MAX_LENGTH_PLUGIN_NAME = 60;

    Logger logger = LoggerManager.getLogger(this);

    FileSystemManager fileSystemManager;
    public ListPluginsCommand(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
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
        listPlugins(evolvenHttpClient, config);
    }


    private void listPlugins(EvolvenHttpClient evolvenHttpClient, EvolvenCliConfig config) throws CommandException {
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

        logger.fine("Calling evolvenHttpClient.uploadPlugin...");
        IHttpRequestResult result = evolvenHttpClient.listPlugins(apiKey);
        if (result.isError()) {
            logger.fine("evolvenHttpClient.listPlugins returned an error...");
            String errorMsg = "Failed to list plugins with the cached details. Login may be required.";
            String reasonPhrase = result.getReasonPhrase();
            if (!StringUtils.isNullOrBlank(reasonPhrase)) {
                errorMsg += "\nReason phrase: " + reasonPhrase;
            }
            logger.log(Level.SEVERE, errorMsg);
            throw new CommandExceptionNotLoggedIn();
        }
        try {
            HashMap<String,List<Map<String,String>>> map = new GsonBuilder().disableHtmlEscaping().create().fromJson(result.getContent(),new TypeToken<HashMap<String,List<Map<String,String>>>>() {}.getType());
            List<Map<String,String>> plugins = map.get("plugins");
            if (plugins.isEmpty()) {
                System.out.println("No plugins");
            } else {
                int length = plugins.stream().map(plugin -> plugin.get("name").length()).filter(len -> len <= MAX_LENGTH_PLUGIN_NAME).max(Integer::compare).orElse(0);
                for (Map<String, String> plugin : plugins) {
                    System.out.printf("%-" + length + "s    | %s%n", plugin.get("name"), plugin.get("id"));
                }
            }
        } catch (Exception e) {
            throw new CommandException("Failed obtain plugins list. " + result.getContent());
        }
    }

    @Override
    public String getName() {
        return "list";
    }
}
