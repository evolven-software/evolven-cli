package com.evolven.httpclient.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.common.StringUtils;
import com.evolven.config.ConfigException;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.httpclient.CachedURLBuilder;
import com.evolven.httpclient.EvolvenHttpClient;
import com.evolven.httpclient.response.EnvironmentsResponse;
import com.evolven.httpclient.http.IHttpRequestResult;
import com.evolven.httpclient.model.Environment;

import java.net.MalformedURLException;
import java.util.Iterator;

public class SearchCommand extends Command {
    private final static String OPTION_QUERY = "query";
    FileSystemManager fileSystemManager;

    public SearchCommand(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
        registerOptions(new String[] {
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
        Iterator<Environment> iterator = search(evolvenHttpClient, config, options.get(OPTION_QUERY));
        String format = "%-30s| %-30s| %s";
        System.out.println(String.format(format, "Name:", "EnvID:", "Host:"));
        while (iterator.hasNext()) {
            Environment env = iterator.next();
            System.out.println(String.format(format, env.getName(), env.getEnvId(), env.getHost()));
        }
    }

    private Iterator<Environment> search(EvolvenHttpClient evolvenHttpClient, EvolvenCliConfig config, String query) throws CommandException {
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
        return response.iterator();
    }

    @Override
    public String getName() {
        return "search";
    }
}