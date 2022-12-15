package com.evolven.filesystem.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;
import com.evolven.common.StringUtils;
import com.evolven.config.ConfigException;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;

public class GetEvolvenCliConfigCommand extends Command {
    public static final String OPTION_KEY = "key";
    public static final String OPTION_ENV = "env";
    public static final String FLAG_ACTIVE_ENV = "activeEnv";
    FileSystemManager fileSystemManager;
    public GetEvolvenCliConfigCommand(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
        registerOptions(new String[] {
                OPTION_ENV,
                OPTION_KEY,
        });
        registerFlag(FLAG_ACTIVE_ENV);
    }

    @Override
    public void execute() throws CommandException {
        EvolvenCliConfig config = fileSystemManager.getConfig();
        boolean activeEnv = flags.get(FLAG_ACTIVE_ENV);
        if (activeEnv) {
            String active = null;
            try {
                active = config.getActiveEnvironment();
            } catch (ConfigException e) {}
            if (!StringUtils.isNullOrBlank(active)) {
                System.out.println("Active environment: " + active);
            }
        }

        String key = options.get(OPTION_KEY);
        if (StringUtils.isNullOrBlank(key)) {
            return;
        }
        String env = options.get(OPTION_ENV);
        String value = null;
        try {
            if (StringUtils.isNullOrBlank(env)) {
                value = config.get(key);
            } else {
                value = config.get(env, key);
            }
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
        if (!StringUtils.isNullOrBlank(value)) {
            System.out.println((StringUtils.isNullOrBlank(env) ? "" : env + ".") + key + ": " + value);
        }
    }



    @Override
    public String getName() {
        return null;
    }
}
