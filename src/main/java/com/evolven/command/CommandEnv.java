package com.evolven.command;

import com.evolven.config.ConfigException;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;

public abstract class CommandEnv extends Command {

	protected CommandEnv(FileSystemManager fileSystemManager) {
		super(fileSystemManager);
		registerOptions(OPTION_ENV);
	}

	protected void setEnvironmentFromConfig(EvolvenCliConfig config) throws CommandException {
		try {
			config.setEnvironmentIfExistsOrActive(options.get(OPTION_ENV));
		} catch (ConfigException e) {
			throw new CommandException("Failed setting environment.", e);
		}
	}
	
	protected boolean isIllegalEnvironmentName(String env) {
		return EvolvenCliConfig.ACTIVE_ENVIRONMENT_KEY.equalsIgnoreCase(env);
	}
}