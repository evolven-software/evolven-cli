package com.evolven.cli.main;

import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;

import java.io.PrintStream;

public class EvolvenCommandsGroup extends EvolvenCommandBase {

    @Override
    public void execute() throws CommandException {
        PrintStream ps = System.err;
        if (help) {
            ps = System.out;
        }
        spec.commandLine().usage(ps);
    }

    @Override
    protected void addParameters() throws InvalidParameterException {}
}
