package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import picocli.CommandLine;

@CommandLine.Command(name = EvolvenCommandConfigSet.COMMAND_NAME)
public class EvolvenCommandConfigSet extends EvolvenCommand {
    final static String COMMAND_NAME = "set";

    @CommandLine.Option(names = {"-a", "--active-env"}, description = "Set active environment from which to get the value.")
    String activeEnv;

    @CommandLine.Option(names = {"-e", "--env"}, description = "The environment from which to get the value.")
    String env;

    @CommandLine.Option(names = {"-k", "--key"}, description = "The key to be set.")
    String key;

    @CommandLine.Option(names = {"-v", "--value"}, description = "The value to be set.")
    String value;


    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    boolean help;

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
