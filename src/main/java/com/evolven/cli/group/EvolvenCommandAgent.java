package com.evolven.cli.group;

import com.evolven.cli.main.EvolvenCommandsGroup;
import picocli.CommandLine;

@CommandLine.Command(
        name = EvolvenCommandAgent.COMMAND_NAME,
        header = "Group of commands related to Evolven Agent.",
        footer = "%nUse 'evolven " + EvolvenCommandAgent.COMMAND_NAME + " <command> --help' to read about a specific subcommand or concept."
)
public class EvolvenCommandAgent extends EvolvenCommandsGroup {
    public static final String COMMAND_NAME = "agent";
}
