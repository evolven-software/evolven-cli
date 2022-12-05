package com.evolven.httpclient.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;
import com.evolven.common.Errors;
import com.evolven.common.StringUtils;
import com.evolven.config.ConfigException;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.httpclient.CachedURLBuilder;
import com.evolven.httpclient.CachedValue;
import com.evolven.httpclient.EvolvenHttpClient;
import com.evolven.httpclient.http.HttpRequestResult;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.MalformedURLException;

public class LoginCommand extends Command {

    FileSystemManager fileSystemManager;
    public static final String OPTION_HOST = "host";
    public static final String OPTION_PORT = "port";
    public static final String OPTION_URL = "url";
    public static final String OPTION_USERNAME = "username";
    public static final String OPTION_PASSWORD = "password";

    public static final String OPTION_ENV = "env";
    public static final String FLAG_SKIP_CACHING = "skipCache";
   public LoginCommand(FileSystemManager fileSystemManager) {

       this.fileSystemManager = fileSystemManager;


       registerOptions(new String[] {
               OPTION_HOST,
               OPTION_PORT,
               OPTION_URL,
               OPTION_USERNAME,
               OPTION_PASSWORD,
               OPTION_ENV,
       });

       registerFlag(FLAG_SKIP_CACHING);
   }

    private void updateCache(CachedValue cachedValue, EvolvenCliConfig config) {
           cachedValue.set(OPTION_USERNAME, Errors.rethrow().wrap(config::setUsername));
           cachedValue.set(OPTION_PASSWORD, Errors.rethrow().wrap(config::setPassword));
           cachedValue.set(OPTION_HOST, Errors.rethrow().wrap(config::setHost));
           cachedValue.set(OPTION_URL, Errors.rethrow().wrap(config::setUrl));
    }

    private String createBaseUrl(CachedValue cachedValue, EvolvenCliConfig config) throws CommandException {
        String host = cachedValue.getOrThrow(
                OPTION_HOST,
                Errors.rethrow().wrap(config::getHost),
                new InvalidParameterException("No host provided."));
        String port = cachedValue.get(
                OPTION_PORT,
                Errors.rethrow().wrap(config::getPort));
        CachedURLBuilder builder = new CachedURLBuilder(config);
        builder.setHost(host);
        builder.setPort(port);
        try {
            return builder.build();
        } catch (MalformedURLException | ConfigException e) {
            throw new CommandException("Failed to construct base URL. " + e.getMessage());
        }
    }

    @Override
    public void execute() throws CommandException {
        EvolvenCliConfig config = fileSystemManager.getConfig();
        try {
            config.setCurrentAndCachedEnvironment(options.get(OPTION_ENV));
        } catch (ConfigException e) {
            throw new CommandException("Failed to set active environment in the cache. " + e.getMessage());
        }

        CachedValue cachedValue = new CachedValue(options);
        if (!flags.get(FLAG_SKIP_CACHING)) {
            updateCache(cachedValue, config);
        }
        String baseUrl = createBaseUrl(cachedValue, config);
        EvolvenHttpClient evolvenHttpClient = new EvolvenHttpClient(baseUrl);
        HttpRequestResult result = login(evolvenHttpClient, cachedValue, config);
        cacheApiKey(result, config);
    }

    private HttpRequestResult login(EvolvenHttpClient evolvenHttpClient, CachedValue cachedValue, EvolvenCliConfig config) throws CommandException {
        String username = cachedValue.getOrThrow(
                OPTION_USERNAME,
                Errors.rethrow().wrap(config::getUsername),
                new InvalidParameterException("No username provided."));
        String password = cachedValue.getOrThrow(
                OPTION_PASSWORD,
                Errors.rethrow().wrap(config::getPassword),
                new InvalidParameterException("No password provided."));
        HttpRequestResult result = evolvenHttpClient.login(username, password);

        if (result.isError()) {
            String errorMsg = "Failed to login with the provided/cached details.";
            String reasonPhrase = result.getReasonPhrase();
            if (!StringUtils.isNullOrBlank(reasonPhrase)) {
                errorMsg += " " + reasonPhrase;
            }
            throw new CommandException(errorMsg);
        }
        return result;
    }

    private void cacheApiKey(HttpRequestResult result, EvolvenCliConfig config) throws CommandException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(result.getContent());
        } catch (ParseException e) {
            throw new CommandException("Failed to parse server's response. " + e.getMessage());
        }
        JSONObject next = (JSONObject) jsonObject.get("Next");
        String apiKey = (String) next.get("ID");
        try {
            config.setApiKey(apiKey);
        } catch (ConfigException e) {
            throw new CommandException("Failed to cache api-key. " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "login";
    }
}
