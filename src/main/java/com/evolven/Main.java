package com.evolven;

import com.evolven.cli.EvolvenCommandLine;
import com.evolven.command.Command;
import com.evolven.filesystem.command.CreateEvolvenCliConfigCommand;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.filesystem.command.LogoutCommand;
import com.evolven.filesystem.command.UpdateEvolvenCliConfigCommand;
import com.evolven.httpclient.command.GetPoliciesCommand;
import com.evolven.httpclient.command.PushPolicyCommand;
import com.evolven.httpclient.http.HttpClient;
import com.evolven.httpclient.command.LoginCommand;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        //System.out.println();
        FileSystemManager fileSystemManager = new FileSystemManager();
        Command[] commands = new Command[] {
                new LoginCommand(fileSystemManager),
                new GetPoliciesCommand(fileSystemManager),
                new PushPolicyCommand(fileSystemManager),
                new LogoutCommand(fileSystemManager),
                new UpdateEvolvenCliConfigCommand(fileSystemManager),
                new CreateEvolvenCliConfigCommand(fileSystemManager),
        };
        //HttpClient.getPoliciesTest();

        System.exit(
                EvolvenCommandLine.execute(
                        Arrays.stream(commands).collect(Collectors.toMap(Command::getName, Function.identity())),
                        args));
    }
}