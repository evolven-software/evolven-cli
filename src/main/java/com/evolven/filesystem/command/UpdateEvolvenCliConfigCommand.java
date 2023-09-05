package com.evolven.filesystem.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.filesystem.FileSystemManager;

public class UpdateEvolvenCliConfigCommand extends Command {

    public UpdateEvolvenCliConfigCommand(FileSystemManager fileSystemManager) {
        super(fileSystemManager);
    }

    @Override
    public void execute() throws CommandException {}

    @Override
    public String getName() {
        return "update-config";
    }
}
