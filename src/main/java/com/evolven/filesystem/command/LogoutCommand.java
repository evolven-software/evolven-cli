package com.evolven.filesystem.command;

import com.evolven.command.CommandEnv;
import com.evolven.command.CommandException;
import com.evolven.common.StringUtils;
import com.evolven.config.ConfigException;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;

public class LogoutCommand extends CommandEnv {

    public LogoutCommand(FileSystemManager fileSystemManager) {
        super(fileSystemManager);
    }

    @Override
    public void execute() throws CommandException {
        EvolvenCliConfig config = getConfig();
        String env = options.get(OPTION_ENV);
        if (StringUtils.isNullOrBlank(env)) {
            throw new CommandException("No environment provided.");
        } else {
            config.setEnvironment(env);
            try {
                config.deleteApiKey(env);
            } catch (ConfigException e) {}
        }
    }

    @Override
    public String getName() {
        return "logout";
    }
}
