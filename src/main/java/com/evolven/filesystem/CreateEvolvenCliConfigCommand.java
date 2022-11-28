package com.evolven.filesystem;

import com.evolven.command.Command;
import com.evolven.command.CommandException;

import java.io.File;

public class CreateEvolvenCliConfigCommand extends Command {
    private FileSystemManager fileSystemManager;
    public static final String FLAG_FORCE = "force";

    public CreateEvolvenCliConfigCommand(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
        registerFlags(new String[] {FLAG_FORCE});
    }

    @Override
    public void execute() throws CommandException {
        File configDirectory = this.fileSystemManager.createConfigDirectory(flags.get(FLAG_FORCE));
        if (configDirectory == null) {
            throw new CommandException("Failed to create configuration files.");
        }
    }

    @Override
    public String getName() {
        return "create-config";
    }
}
