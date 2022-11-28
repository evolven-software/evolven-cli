package com.evolven.httpclient;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.filesystem.FileSystemManager;

public class GetPoliciesCommand extends Command {
    FileSystemManager fileSystemManager;
    public GetPoliciesCommand(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
    }
    @Override
    public void execute() throws CommandException {

    }

    @Override
    public String getName() {
        return "get-policies";
    }
}
