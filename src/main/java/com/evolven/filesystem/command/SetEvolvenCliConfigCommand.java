package com.evolven.filesystem.command;

import com.evolven.command.CommandEnv;
import com.evolven.command.CommandException;
import com.evolven.common.StringUtils;
import com.evolven.config.ConfigException;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.logging.LoggerManager;

import java.util.logging.Logger;

public class SetEvolvenCliConfigCommand extends CommandEnv {

    private final Logger logger = LoggerManager.getLogger(this);

    public SetEvolvenCliConfigCommand(FileSystemManager fileSystemManager) {
        super(fileSystemManager);

        registerOptions(
                OPTION_ACTIVE_ENV,
                OPTION_KEY,
                OPTION_VALUE
        );
    }

    @Override
    public void execute() throws CommandException {
        EvolvenCliConfig config = getConfig();
        String activeEnv = options.get(OPTION_ACTIVE_ENV);
        if (!StringUtils.isNullOrBlank(activeEnv)) {
            if (isIllegalEnvironmentName(activeEnv)) {
                throw new CommandException("Illegal environment name: \"" + activeEnv + "\"");
            }
            try {
                config.setActiveEnvironment(activeEnv);
            } catch (ConfigException e) {
                throw new CommandException("Failed to set active environment.", e);
            }
        }

        String key = options.get(OPTION_KEY);
        if (StringUtils.isNullOrBlank(key)) {
            logger.info("No key is provided.");
            return;
        }

        String value = options.get(OPTION_VALUE);
        String env = options.get(OPTION_ENV);
        if (StringUtils.isNullOrBlank(value)) {
            logger.info("No value is provided, invoking remove.");
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
            logger.info("No env is provided.");
            config.remove(key);
        } else {
            config.remove(env, key);
        }
    }

    private void set(String env, String key, String value, EvolvenCliConfig config) throws ConfigException {
        if (StringUtils.isNullOrBlank(env)) {
            logger.info("No env is provided.");
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
