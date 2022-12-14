package com.evolven.cli;

import picocli.CommandLine;

@CommandLine.Command(
        name = EvolvenCommandPolicy.COMMAND_NAME,
        header = "Group of commands related to Evolven policies.",
        footer = "%nUse 'evolven " + EvolvenCommandPolicy.COMMAND_NAME + " <command> --help' to read about a specific subcommand or concept."
)
public class EvolvenCommandPolicy extends EvolvenCommandsGroup {
    public static final String COMMAND_NAME = "policy";
}
