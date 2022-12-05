package com.evolven.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
}
