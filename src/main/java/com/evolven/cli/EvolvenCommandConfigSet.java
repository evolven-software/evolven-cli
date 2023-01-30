package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import picocli.CommandLine;

@CommandLine.Command(name = EvolvenCommandConfigSet.COMMAND_NAME)
public class EvolvenCommandConfigSet extends EvolvenCommand {
    final static String COMMAND_NAME = "set";

    @CommandLine.Option(names = {"-a", "--active-env"}, description = "Set active environment from which to get the value.")
    protected String activeEnv;

    @CommandLine.Option(names = {"-e", "--env"}, description = "The environment from which to get the value.")
    protected String env;

    @CommandLine.Option(names = {"-k", "--key"}, description = "The key to be set.")
    protected String key;

    @CommandLine.Option(names = {"-v", "--value"}, description = "The value to be set.")
    protected String value;


    @CommandLine.Spec
    protected CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    protected boolean help;

    public EvolvenCommandConfigSet(Command command) {
        super(command);
    }

    @Override
    public void execute() throws CommandException {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        addOption(env, this);
        addOption(activeEnv, this);
        addOption(key, this);
        addOption(value, this);
        invokeHandler();
    }
}
