package com.evolven.httpclient.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;
import com.evolven.common.StringUtils;
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
    public static final String OPTION_TIMEOUT = "timeout";
    public static final String FLAG_SKIP_CACHING = "skipCache";
   public LoginCommand(FileSystemManager fileSystemManager) {

       this.fileSystemManager = fileSystemManager;


       registerOptions(new String[] {
               OPTION_HOST,
               OPTION_PORT,
               OPTION_URL,
               OPTION_USERNAME,
               OPTION_PASSWORD,
               OPTION_TIMEOUT,
       });

       registerFlag(FLAG_SKIP_CACHING);
   }

    private void updateCache(CachedValue cachedValue, EvolvenCliConfig config) {
        cachedValue.set(OPTION_USERNAME, config::setUsername);
        cachedValue.set(OPTION_PASSWORD, config::setPassword);
        cachedValue.set(OPTION_HOST, config::setHost);
        cachedValue.set(OPTION_URL, config::setUrl);
    }

    private String createBaseUrl(CachedValue cachedValue, EvolvenCliConfig config) throws CommandException {
        String host = cachedValue.getOrThrow(
                OPTION_HOST,
                config::getHost,
                new InvalidParameterException("No host provided."));
        String port = cachedValue.get(
                OPTION_PORT,
                config::getPort);
        CachedURLBuilder builder = new CachedURLBuilder(config);
        builder.setHost(host);
        builder.setPort(port);
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
        if (!flags.get(FLAG_SKIP_CACHING)) {
            updateCache(cachedValue, config);
        }
        String baseUrl = createBaseUrl(cachedValue, config);
        EvolvenHttpClient evolvenHttpClient = new EvolvenHttpClient(baseUrl);
        login(evolvenHttpClient, cachedValue, config);
    }

    private void login(EvolvenHttpClient evolvenHttpClient, CachedValue cachedValue, EvolvenCliConfig config) throws CommandException {
        String username = cachedValue.getOrThrow(
                OPTION_USERNAME,
                config::getUsername,
                new InvalidParameterException("No username provided."));
        String password = cachedValue.getOrThrow(
                OPTION_PASSWORD,
                config::getPassword,
                new InvalidParameterException("No password provided."));
        HttpRequestResult result = evolvenHttpClient.login(username, password);

        if (result.getStatusCode() != 200) {
            String errorMsg = "Failed to login with the provided/cached details.";
            String reasonPhrase = result.getReasonPhrase();
            if (!StringUtils.isNullOrBlank(reasonPhrase)) {
                errorMsg += " " + reasonPhrase;
            }
            throw new CommandException(errorMsg);
        }
        result.print(System.out);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(result.getContent());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        JSONObject next = (JSONObject) jsonObject.get("Next");
        String apiKey = (String) next.get("ID");
        config.setApiKey(apiKey);
    }

    @Override
    public String getName() {
        return "login";
    }
}
