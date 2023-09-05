package com.evolven.filesystem.command;

import com.evolven.command.CommandEnv;
import com.evolven.command.CommandException;
import com.evolven.common.StringUtils;
import com.evolven.config.ConfigException;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;

public class GetEvolvenCliConfigCommand extends CommandEnv {

    public GetEvolvenCliConfigCommand(FileSystemManager fileSystemManager) {
        super(fileSystemManager);

        registerOptions(
                OPTION_KEY
        );

        registerFlags(
                OPTION_ACTIVE_ENV
        );
    }

    @Override
    public void execute() throws CommandException {
        EvolvenCliConfig config = getConfig();
        boolean activeEnv = flags.get(OPTION_ACTIVE_ENV);
        if (activeEnv) {
            try {
                String active = config.getActiveEnvironment();
                if (!StringUtils.isNullOrBlank(active)) {
                    System.out.println("Active environment: \"" + active + "\"");
                } else {
                    System.out.println("No active environment");
                }
            } catch (ConfigException e) {
                throw new CommandException("Failed to load active environment.", e);
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
            throw new CommandException("Failed to get config value.", e);
        }
        System.out.println((StringUtils.isNullOrBlank(env) ? "" : env + ".") + key + ": \"" + (StringUtils.isNullOrBlank(value) ? "" : value) + "\"");
    }

    @Override
    public String getName() {
        return null;
    }
}
