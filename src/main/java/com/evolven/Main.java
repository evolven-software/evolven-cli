package com.evolven;

import com.evolven.cli.*;
import com.evolven.filesystem.command.CreateEvolvenCliConfigCommand;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.filesystem.command.LogoutCommand;
import com.evolven.httpclient.command.PullPolicyCommand;
import com.evolven.httpclient.command.PushPolicyCommand;
import com.evolven.httpclient.command.LoginCommand;
import picocli.CommandLine;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        FileSystemManager fileSystemManager = new FileSystemManager();
        CommandLine cli = new CommandLine(new EvolvenCommandLine())
                .addSubcommand("login", new EvolvenCommandLogin(new LoginCommand(fileSystemManager)))
                .addSubcommand("logout", new EvolvenCommandLogout(new LogoutCommand(fileSystemManager)))
                .addSubcommand("config", new CommandLine(new EvolvenCommandConfig())
                        .addSubcommand("create", new EvolvenCommandConfigCreate(new CreateEvolvenCliConfigCommand(fileSystemManager)))
                        .addSubcommand("set", new EvolvenCommandConfigCreate(new CreateEvolvenCliConfigCommand(fileSystemManager)))
                        .addSubcommand("get", new EvolvenCommandConfigCreate(new CreateEvolvenCliConfigCommand(fileSystemManager)))
                    )
                .addSubcommand("policy", new CommandLine(new EvolvenPolicy())
                        .addSubcommand("pull", new EvolvenCommandPolicyPull(new PullPolicyCommand(fileSystemManager)))
                        .addSubcommand("push", new EvolvenCommandPolicyPush(new PushPolicyCommand(fileSystemManager)))
                );
        System.exit(cli.execute(args));
    }
}