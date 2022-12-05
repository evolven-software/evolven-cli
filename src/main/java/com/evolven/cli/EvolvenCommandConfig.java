package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;
import com.evolven.filesystem.FileSystemManager;
import picocli.CommandLine;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

@CommandLine.Command(name = "config", header = "Group of commands related to the configurations")
public class EvolvenCommandConfig {

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    boolean help;

    @Spec
    CommandSpec spec;

    public void run() {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        spec.commandLine().usage(System.err);
    }

}
