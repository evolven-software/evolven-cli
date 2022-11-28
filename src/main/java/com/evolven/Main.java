package com.evolven;

import com.evolven.cli.EvolvenCommandLine;
import com.evolven.command.Command;
import com.evolven.filesystem.CreateEvolvenCliConfigCommand;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.filesystem.LogoutCommand;
import com.evolven.filesystem.UpdateEvolvenCliConfigCommand;
import com.evolven.httpclient.GetPoliciesCommand;
import com.evolven.httpclient.LoginCommand;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        //System.out.println();
        FileSystemManager fileSystemManager = new FileSystemManager();
        Command[] commands = new Command[] {
                new LoginCommand(),
                new GetPoliciesCommand(),
                new LogoutCommand(fileSystemManager),
                new UpdateEvolvenCliConfigCommand(fileSystemManager),
                new CreateEvolvenCliConfigCommand(fileSystemManager),
        };

        System.exit(
                EvolvenCommandLine.execute(
                        Arrays.stream(commands).collect(Collectors.toMap(Command::getName, Function.identity())),
                        args));
    }
}