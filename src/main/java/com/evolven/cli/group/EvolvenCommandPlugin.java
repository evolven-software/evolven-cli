package com.evolven.cli.group;

import com.evolven.cli.main.EvolvenCommandsGroup;
import picocli.CommandLine;

@CommandLine.Command(
        name = EvolvenCommandPlugin.COMMAND_NAME,
        header = "Group of commands related to plugins management",
        footer = "%nUse 'evolven " + EvolvenCommandPolicy.COMMAND_NAME + " <command> --help' to read about a specific subcommand or concept."
)
public class EvolvenCommandPlugin extends EvolvenCommandsGroup {
    public static final String COMMAND_NAME = "plugin";
}
