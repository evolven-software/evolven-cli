package com.evolven.filesystem.command;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.filesystem.FileSystemManager;

import java.io.File;

public class LogoutCommand extends Command {
    FileSystemManager fileSystemManager;
    public LogoutCommand(FileSystemManager fileSystemManager) {
        this.fileSystemManager  = fileSystemManager;
    }

    @Override
    public void execute() throws CommandException {
    }

    @Override
    public String getName() {
        return "logout";
    }
}
