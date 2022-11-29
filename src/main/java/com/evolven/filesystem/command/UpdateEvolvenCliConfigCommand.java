package com.evolven.filesystem.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.filesystem.FileSystemManager;

public class UpdateEvolvenCliConfigCommand extends Command {
    private FileSystemManager fileSystemManager;
    public UpdateEvolvenCliConfigCommand(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
    }

    @Override
    public void execute() throws CommandException {

    }

    @Override
    public String getName() {
        return "update-config";
    }
}
