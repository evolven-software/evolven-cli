package com.evolven.cli.main;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import picocli.CommandLine;

public abstract class EvolvenCommandEnv extends EvolvenCommandBase {
    
    public EvolvenCommandEnv() {
        super();
    }
    public EvolvenCommandEnv(Command command) {
        super(command);
    }
    
    @CommandLine.Option(names = {"-e", "--env"}, description = "Environment label.")
    protected String env;
    
    @Override
    protected void executeNoHelp() throws CommandException {
        addOption(env, this);
        addParameters();
        invokeHandler();
    }
}
