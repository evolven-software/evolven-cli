package com.evolven.cli.main;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

public abstract class EvolvenCommandBase extends EvolvenCommand {
    
    public EvolvenCommandBase() {
        super();
    }
    public EvolvenCommandBase(Command command) {
        super(command);
    }

    @CommandLine.Spec
    protected CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    protected boolean help;
    
    @Override
    public void execute() throws CommandException {
        if (help) {
            spec.commandLine().usage(System.out);
        } else {
            executeNoHelp();
        }
    }
    
    protected abstract void addParameters() throws InvalidParameterException;
    
    protected void executeNoHelp() throws CommandException {
        addParameters();
        invokeHandler();
    }
}
