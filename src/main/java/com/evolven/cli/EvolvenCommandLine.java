package com.evolven.cli;

import picocli.CommandLine.Command;

@Command(name = "evolven", mixinStandardHelpOptions = true, version = "Evolven CLI 1.0",
        description = "Evolven command line interface.",
        commandListHeading = "%nCommands:%n%nThe most commonly used commands are:%n",
        footer = "%nUse 'evolven <command> [<command>] --help' to read about a specific subcommand or concept."
)
public class EvolvenCommandLine extends EvolvenCommandsGroup {}
