package com.evolven;

import com.evolven.cli.*;
import com.evolven.cli.exception.ExecutionExceptionHandler;
import com.evolven.filesystem.command.CreateEvolvenCliConfigCommand;
import com.evolven.filesystem.FileSystemManager;
import com.evolven.filesystem.command.GetEvolvenCliConfigCommand;
import com.evolven.filesystem.command.LogoutCommand;
import com.evolven.filesystem.command.SetEvolvenCliConfigCommand;
import com.evolven.httpclient.command.*;
import com.evolven.logging.LoggerManager;
import picocli.CommandLine;

import java.io.IOException;
import java.util.logging.Logger;

public class Main {
    static Logger logger;

    public static void main(String[] args) throws IOException {
        FileSystemManager fileSystemManager = new FileSystemManager();
        logger = LoggerManager.getLogger(Main.class);
        logger.fine("Creating cli interface...");
        CommandLine cli = new CommandLine(new EvolvenCommandLine())
                .addSubcommand("login", new EvolvenCommandLogin(new LoginCommand(fileSystemManager)))
                .addSubcommand("logout", new EvolvenCommandLogout(new LogoutCommand(fileSystemManager)))
                .addSubcommand("search", new EvolvenCommandSearch(new SearchCommand(fileSystemManager)))
                .addSubcommand("config", new CommandLine(new EvolvenCommandConfig())
                        .addSubcommand("create", new EvolvenCommandConfigCreate(new CreateEvolvenCliConfigCommand(fileSystemManager)))
                        .addSubcommand("set", new EvolvenCommandConfigSet(new SetEvolvenCliConfigCommand(fileSystemManager)))
                        .addSubcommand("get", new EvolvenCommandConfigGet(new GetEvolvenCliConfigCommand(fileSystemManager)))
                )
                .addSubcommand("plugin", new CommandLine(new EvolvenCommandPlugin())
                        .addSubcommand("upload", new EvolvenCommandPluginUpload(new UploadPluginCommand(fileSystemManager)))
                )
                .addSubcommand("policy", new CommandLine(new EvolvenCommandPolicy())
                        .addSubcommand("pull", new EvolvenCommandPolicyPull(new PullPolicyCommand(fileSystemManager)))
                        .addSubcommand("push", new EvolvenCommandPolicyPush(new PushPolicyCommand(fileSystemManager)))
                        .addSubcommand("test", new EvolvenCommandPolicyTest(new TestPolicyCommand(fileSystemManager)))
                );

        logger.fine("Executing command...");
        cli.setExecutionExceptionHandler(new ExecutionExceptionHandler());
        System.exit(cli.execute(args));
    }
}