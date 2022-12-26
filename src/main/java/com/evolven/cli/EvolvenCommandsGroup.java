package com.evolven.cli;

import com.evolven.command.CommandException;
import picocli.CommandLine;
import java.io.PrintStream;

public class EvolvenCommandsGroup extends EvolvenCommand {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    boolean help;

    @Override
    public void execute() throws CommandException {
        PrintStream ps = System.err;
        if (help) {
            ps = System.out;
        }
        spec.commandLine().usage(ps);
    }
}
