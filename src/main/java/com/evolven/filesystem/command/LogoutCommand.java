package com.evolven.filesystem.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.common.StringUtils;
import com.evolven.config.ConfigException;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;

public class LogoutCommand extends Command {

    public static final String OPTION_ENV = "env";
    FileSystemManager fileSystemManager;

    public LogoutCommand(FileSystemManager fileSystemManager) {
        this.fileSystemManager  = fileSystemManager;
        registerOption(OPTION_ENV);
    }

    @Override
    public void execute() throws CommandException {
        String env = options.get(OPTION_ENV);
        EvolvenCliConfig config = fileSystemManager.getConfig();
        try {
            if (StringUtils.isNullOrBlank(env)
                    && StringUtils.isNullOrBlank(env = config.getActiveEnvironment())) return;
            config.deleteApiKey(env);
        } catch (ConfigException e) {}
    }

    @Override
    public String getName() {
        return "logout";
    }
}
