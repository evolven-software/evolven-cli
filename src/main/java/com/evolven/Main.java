package com.evolven;

import com.evolven.cli.*;
import com.evolven.filesystem.command.CreateEvolvenCliConfigCommand;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.filesystem.command.LogoutCommand;
import com.evolven.httpclient.command.*;
import com.evolven.logging.Logger;
import picocli.CommandLine;

import java.io.IOException;

public class Main {

    static Logger logger;

    public static void main(String[] args) throws IOException {
        FileSystemManager fileSystemManager = new FileSystemManager();
        logger = new Logger(Main.class.getName());
        logger.info("main test");
        logger.info("Creating cli interface...");
        CommandLine cli = new CommandLine(new EvolvenCommandLine())
                .addSubcommand("login", new EvolvenCommandLogin(new LoginCommand(fileSystemManager)))
                .addSubcommand("logout", new EvolvenCommandLogout(new LogoutCommand(fileSystemManager)))
                .addSubcommand("search", new EvolvenCommandSearch(new SearchCommand(fileSystemManager)))
                .addSubcommand("config", new CommandLine(new EvolvenCommandConfig())
                        .addSubcommand("create", new EvolvenCommandConfigCreate(new CreateEvolvenCliConfigCommand(fileSystemManager)))
                        .addSubcommand("set", new EvolvenCommandConfigCreate(new CreateEvolvenCliConfigCommand(fileSystemManager)))
                        .addSubcommand("get", new EvolvenCommandConfigCreate(new CreateEvolvenCliConfigCommand(fileSystemManager)))
                    )
                .addSubcommand("policy", new CommandLine(new EvolvenPolicy())
                        .addSubcommand("pull", new EvolvenCommandPolicyPull(new PullPolicyCommand(fileSystemManager)))
                        .addSubcommand("push", new EvolvenCommandPolicyPush(new PushPolicyCommand(fileSystemManager)))
                        .addSubcommand("test", new EvolvenCommandPolicyTest(new TestPolicyCommand(fileSystemManager)))
                );

        logger.info("Executing command...");
        System.exit(cli.execute(args));
    }
}