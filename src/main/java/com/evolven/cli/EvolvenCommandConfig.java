package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;
import com.evolven.filesystem.FileSystemManager;
import picocli.CommandLine;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

@CommandLine.Command(
        name = EvolvenCommandConfig.COMMAND_NAME,
        header = "Group of commands related to the configurations",
        footer = "%nUse 'evolven " + EvolvenCommandPolicy.COMMAND_NAME + " <command> --help' to read about a specific subcommand or concept."
)
public class EvolvenCommandConfig extends EvolvenCommandsGroup {
    public static final String COMMAND_NAME = "config";
}
