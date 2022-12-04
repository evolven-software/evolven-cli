package com.evolven.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

import java.util.Arrays;
import java.util.Map;

@Command(name = "evolven", mixinStandardHelpOptions = true, version = "Evolven CLI 1.0",
        description = "Evolven command line interface",
        commandListHeading = "%nCommands:%n%nThe most commonly used commands are:%n",
        footer = "%nSee 'evolven help <command>' to read about a specific subcommand or concept."
     //   ,subcommands = {
     //           EvolvenGetPolicies.class,
     //           EvolvenLogin.class,
     //           EvolvenLogout.class,
     //           CommandLine.HelpCommand.class
     //   }
)
public class EvolvenCommandLine implements Runnable {

    @Spec
    CommandSpec spec;

    //@Parameters(arity = "1..3", descriptions = "one to three Files")
    //File[] files;

    @Override
    public void run() {
        // if the command was invoked without subcommand, show the usage help
        spec.commandLine().usage(System.err);
    }

    private static EvolvenCommand[] getEvolvenCommands() {
        return new EvolvenCommand[] {
                new EvolvenLogin(),
                new EvolvenGetPolicies(),
                new EvolvenPushPolicy(),
                new EvolvenUpdateConfig(),
                new EvolvenCreateConfig(),
                new EvolvenLogout(),
        };
    }

    private static void addExecutors(CommandLine cli, EvolvenCommand[] evolvenCommands, Map<String, com.evolven.command.Command> commands) {
        Arrays.stream(evolvenCommands).forEach(evolvenCommand -> {
            String commandName = evolvenCommand.getClass().getAnnotation(Command.class).name();
            evolvenCommand.addExecutor(commands.get(commandName));
            cli.addSubcommand(commandName, evolvenCommand);
        });
    }

    public static CommandLine createCLI(Map<String, com.evolven.command.Command> commands) {
        CommandLine cli = new CommandLine(new EvolvenCommandLine());
        EvolvenCommand[] evolvenCommands = getEvolvenCommands();
        addExecutors(cli, evolvenCommands, commands);
        return cli;
    }

    public static int execute(Map<String, com.evolven.command.Command> commands, String[] args) {
        return createCLI(commands).execute(args);
    }

    public static int execute(CommandLine cli, String[] args) {
        return cli.execute(args);
    }

}
