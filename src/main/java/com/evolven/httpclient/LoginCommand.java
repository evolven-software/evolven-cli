package com.evolven.httpclient;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.filesystem.FileSystemManager;

import java.util.Arrays;

public class LoginCommand extends Command {
    FileSystemManager fileSystemManager;
   public LoginCommand(FileSystemManager fileSystemManager) {
       this.fileSystemManager = fileSystemManager;
       registerOptions(new String[] {
               "host",
               "port",
               "url",
               "username",
               "password",
               "skip-cache",
       });
   }

    @Override
    public void execute() throws CommandException {

    }

    @Override
    public String getName() {
        return "login";
    }
}
