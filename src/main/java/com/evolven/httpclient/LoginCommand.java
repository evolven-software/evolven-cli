package com.evolven.httpclient;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.filesystem.FileSystemManager;

public class LoginCommand extends Command {

    //https://host13.evolven.com/enlight.server/next/api?action=login
 //   json:
 //           true
 //   user:
 //   evolven
 //   pass:
 //   Mdls1997
 //   isEncrypted:
 //           false

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
       registerFlag("skipCache");
   }

    @Override
    public void execute() throws CommandException {


    }

    @Override
    public String getName() {
        return "login";
    }
}
