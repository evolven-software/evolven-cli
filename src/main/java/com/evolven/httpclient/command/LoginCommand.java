package com.evolven.httpclient.command;

import com.evolven.command.CommandEnv;
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
import com.evolven.httpclient.http.IHttpRequestResult;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.MalformedURLException;

public class LoginCommand extends CommandEnv {

    public static final String FLAG_SKIP_CACHING = "skipCache";
    
   public LoginCommand(FileSystemManager fileSystemManager) {
       super(fileSystemManager);

       registerOptions(
               OPTION_HOST,
		       OPTION_PORT,
		       OPTION_URL,
		       OPTION_USERNAME,
		       OPTION_PASSWORD,
		       OPTION_SCHEMA
       );

       registerFlags(
               FLAG_SKIP_CACHING
       );
   }
    
    private void updateCache(CachedValue cachedValue, EvolvenCliConfig config) {
       cachedValue.set(OPTION_USERNAME, Errors.rethrow().wrap(config::setUsername));
       cachedValue.set(OPTION_HOST, Errors.rethrow().wrap(config::setHost));
       cachedValue.set(OPTION_URL, Errors.rethrow().wrap(config::setUrl));
       cachedValue.set(OPTION_SCHEMA, Errors.rethrow().wrap(config::setSchema));
       cachedValue.set(OPTION_PORT, Errors.rethrow().wrap(config::setPort));
    }
    
    private String createBaseUrl(CachedValue cachedValue, EvolvenCliConfig config) throws CommandException {
        CachedURLBuilder builder = new CachedURLBuilder(config);

        String baseUrl = cachedValue.get(
                OPTION_URL,
                Errors.rethrow().wrap(config::getBaseUrl));
        builder.setBaseUrl(baseUrl);
        if (baseUrl == null) {
            builder.setHost(cachedValue.getOrThrow(
                    OPTION_HOST,
                    Errors.rethrow().wrap(config::getHost),
                    new InvalidParameterException("No host or url provided.")));
            builder.setPort(cachedValue.get(
                    OPTION_PORT,
                    Errors.rethrow().wrap(config::getPort)));
            builder.setScheme(cachedValue.get(
                    OPTION_SCHEMA,
                    Errors.rethrow().wrap(config::getSchema)));
        }

        try {
            return builder.build();
        } catch (MalformedURLException | ConfigException e) {
            throw new CommandException("Failed to construct base URL.", e);
        }
    }
    
    @Override
    public void execute() throws CommandException {
        EvolvenCliConfig config = getConfig();
        String env = options.get(OPTION_ENV);
        if (StringUtils.isNullOrBlank(env)) {
            try {
                env = config.getActiveEnvironment();
                if (StringUtils.isNullOrBlank(env)) {
                    throw new CommandException("No cached environment value (use \"env\" option).");
                }
            } catch (ConfigException e) {
                throw new CommandException("Failed getting active environment.", e);
            }
        } else if (isIllegalEnvironmentName(env)) {
            throw new CommandException("Illegal environment name: \"" + env + "\"");
        }
        try {
            config.setActiveAndCachedEnvironment(env);
        } catch (ConfigException e) {
            throw new CommandException("Failed setting active environment.", e);
        }
        CachedValue cachedValue = new CachedValue(options);
        if (!flags.get(FLAG_SKIP_CACHING)) {
            updateCache(cachedValue, config);
        }
        String baseUrl = createBaseUrl(cachedValue, config);
        EvolvenHttpClient evolvenHttpClient = new EvolvenHttpClient(baseUrl);
        IHttpRequestResult result = login(evolvenHttpClient, cachedValue, config);
        cacheApiKey(result, config);
        System.out.printf("Login successful!");
    }


    private IHttpRequestResult login(EvolvenHttpClient evolvenHttpClient, CachedValue cachedValue, EvolvenCliConfig config) throws CommandException {
        String username = cachedValue.getOrThrow(
                OPTION_USERNAME,
                Errors.rethrow().wrap(config::getUsername),
                new InvalidParameterException("No username provided."));

        IHttpRequestResult result = evolvenHttpClient.login(username, options.get(OPTION_PASSWORD));

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

    private void cacheApiKey(IHttpRequestResult result, EvolvenCliConfig config) throws CommandException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(result.getContent());
        } catch (ParseException e) {
            throw new CommandException("Failed to parse server's response.", e);
        }
        JSONObject next = (JSONObject) jsonObject.get("Next");
        String apiKey = (String) next.get("ID");
        try {
            config.setApiKey(apiKey);
        } catch (ConfigException e) {
            throw new CommandException("Failed to cache api-key.", e);
        }
    }

    @Override
    public String getName() {
        return "login";
    }
}
