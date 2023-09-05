package com.evolven.filesystem.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.filesystem.FileSystemManager;

import java.io.File;
import java.io.IOException;

public class CreateEvolvenCliConfigCommand extends Command {
    public static final String FLAG_FORCE = "force";

    public CreateEvolvenCliConfigCommand(FileSystemManager fileSystemManager) {
        super(fileSystemManager);

        registerFlags(
                FLAG_FORCE
        );
    }

    @Override
    public void execute() throws CommandException {
        File configDirectory = this.fileSystemManager.createConfigDirectory(flags.get(FLAG_FORCE));
        if (configDirectory == null) {
            throw new CommandException("Failed to create configuration files.");
        }
        try {
            this.fileSystemManager.dumpInitialPolicyConfig();
        } catch (IOException e) {
            throw new CommandException("Failed to create initial policy config.", e);
        }
    }

    @Override
    public String getName() {
        return "create-config";
    }
}
