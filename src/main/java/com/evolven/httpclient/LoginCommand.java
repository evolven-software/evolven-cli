package com.evolven.httpclient;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.filesystem.FileSystemManager;

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
               "timeout",
       });
       registerFlags(new String[] {"skipCache"});
   }

    @Override
    public void execute() throws CommandException {

    }

    @Override
    public String getName() {
        return "login";
    }
}
