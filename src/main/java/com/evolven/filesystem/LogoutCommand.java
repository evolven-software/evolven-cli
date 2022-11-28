package com.evolven.filesystem;

import com.evolven.command.Command;
import com.evolven.command.CommandException;

import java.io.File;

public class LogoutCommand extends Command {
    FileSystemManager fileSystemManager;
    public LogoutCommand(FileSystemManager fileSystemManager) {
        this.fileSystemManager  = fileSystemManager;
    }

    @Override
    public void execute() throws CommandException {
        this.fileSystemManager.invalidateApiKey();
    }

    @Override
    public String getName() {
        return "logout";
    }
}
