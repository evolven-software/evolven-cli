package com.evolven.filesystem.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.common.StringUtils;
import com.evolven.config.ConfigException;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.logging.Logger;

public class SetEvolvenCliConfigCommand extends Command {

    public static final String OPTION_KEY = "key";
    public static final String OPTION_ENV = "env";
    public static final String OPTION_VALUE = "value";
    public static final String OPTION_ACTIVE_ENV = "activeEnv";
    FileSystemManager fileSystemManager;
    Logger logger = new Logger(this);

    public SetEvolvenCliConfigCommand(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
        registerOptions(new String[] {
                OPTION_ENV,
                OPTION_KEY,
                OPTION_VALUE,
                OPTION_ACTIVE_ENV,
        });
    }

    @Override
    public void execute() throws CommandException {
        EvolvenCliConfig config = fileSystemManager.getConfig();
        String activeEnv = options.get(OPTION_ACTIVE_ENV);
        if (!StringUtils.isNullOrBlank(activeEnv)) {
            try {
                config.setActiveEnvironment(activeEnv);
            } catch (ConfigException e) {
                throw new CommandException("Failed to set active environment.", e);
            }
        }

        String key = options.get(OPTION_KEY);
        if (StringUtils.isNullOrBlank(key)) {
            logger.debug("No key is provided.");
            return;
        }

        String value = options.get(OPTION_VALUE);
        String env = options.get(OPTION_ENV);
        if (StringUtils.isNullOrBlank(value)) {
            logger.debug("No value is provided, invoking remove.");
            try {
                remove(env, key, config);
            } catch (ConfigException e) {
                throw new CommandException("Failed to remove key: " + key + ".", e);
            }
        } else {
            try {
                set(env, key, value, config);
            } catch (ConfigException e) {
                throw new CommandException("Failed to remove key: " + key + ".", e);
            }

        }
    }


    private void remove(String env, String key, EvolvenCliConfig config) throws ConfigException {
        if (StringUtils.isNullOrBlank(env)) {
            logger.debug("No env is provided.");
            config.remove(key);
        } else {
            config.remove(env, key);
        }
    }

    private void set(String env, String key, String value, EvolvenCliConfig config) throws ConfigException {
        if (StringUtils.isNullOrBlank(env)) {
            logger.debug("No env is provided.");
            config.set(key, value);
        } else {
            config.set(env, key, value);
        }
    }

    @Override
    public String getName() {
        return null;
    }
}