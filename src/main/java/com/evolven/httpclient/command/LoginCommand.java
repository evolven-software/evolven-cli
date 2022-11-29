package com.evolven.httpclient.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.httpclient.CachedURLBuilder;
import com.evolven.httpclient.EvolvenHttpClient;

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


    @Override
    public void execute() throws CommandException {
       if (!flags.get(FLAG_SKIP_CACHING)) {
           EvolvenCliConfig config = fileSystemManager.getEvolvenCliConfig();
           if (!options.get(OPTION_USERNAME).isEmpty()) {
               config.setUsername(options.get(OPTION_USERNAME));
           }
           if (!options.get(OPTION_HOST).isEmpty()) {
               config.setHost(options.get(OPTION_HOST));
           }
           if (!options.get(OPTION_PORT).isEmpty()) {
               config.setPort(options.get(OPTION_PORT));
           }
           if (!options.get(OPTION_URL).isEmpty()) {
               config.setUrl(options.get(OPTION_URL));
           }
       }
       CachedURLBuilder builder = new CachedURLBuilder(fileSystemManager.getConfig());
       builder.setHost(options.get(OPTION_HOST));
        builder.setHost(options.get(OPTION_HOST));
        EvolvenHttpClient evolvenHttpClient = null;
        try {
            evolvenHttpClient = new EvolvenHttpClient(builder.build());
        } catch (MalformedURLException e) {
            throw new CommandException("brrr" + e.getMessage());
        }
        evolvenHttpClient.login(options.get(OPTION_USERNAME), options.get(OPTION_PASSWORD));
    }

    @Override
    public String getName() {
        return "login";
    }
}
