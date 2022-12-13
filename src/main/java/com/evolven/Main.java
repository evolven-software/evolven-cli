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

        int status = 1;
        try {
            FileSystemManager fileSystemManager = new FileSystemManager();
            logger = new Logger(Main.class.getName());
            logger.debug("Creating cli interface...");
            CommandLine cli = new CommandLine(new EvolvenCommandLine())
                    .addSubcommand("login", new EvolvenCommandLogin(new LoginCommand(fileSystemManager)))
                    .addSubcommand("logout", new EvolvenCommandLogout(new LogoutCommand(fileSystemManager)))
                    .addSubcommand("search", new EvolvenCommandSearch(new SearchCommand(fileSystemManager)))
                    .addSubcommand("config", new CommandLine(new EvolvenCommandConfig())
                            .addSubcommand("create", new EvolvenCommandConfigCreate(new CreateEvolvenCliConfigCommand(fileSystemManager)))
                            .addSubcommand("set", new EvolvenCommandConfigCreate(new CreateEvolvenCliConfigCommand(fileSystemManager)))
                            .addSubcommand("get", new EvolvenCommandConfigCreate(new CreateEvolvenCliConfigCommand(fileSystemManager)))
                    )
                    .addSubcommand("policy", new CommandLine(new EvolvenCommandPolicy())
                            .addSubcommand("pull", new EvolvenCommandPolicyPull(new PullPolicyCommand(fileSystemManager)))
                            .addSubcommand("push", new EvolvenCommandPolicyPush(new PushPolicyCommand(fileSystemManager)))
                            .addSubcommand("test", new EvolvenCommandPolicyTest(new TestPolicyCommand(fileSystemManager)))
                    );

            logger.debug("Executing command...");
            cli.setExecutionExceptionHandler(new ExecutionExceptionHandler());
            status = cli.execute(args);
        } catch (EvolvenCommandException e) {
            String err = "FAILED\n" + e.getMessage();
            logger.error(err);
            System.err.println(err);
        }
        System.exit(status);
    }
}