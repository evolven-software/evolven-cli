package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import picocli.CommandLine;


@CommandLine.Command(name = EvolvenCommandConfigGet.COMMAND_NAME)
public class EvolvenCommandConfigGet extends EvolvenCommand {
    final static String COMMAND_NAME = "get";


    @CommandLine.Option(names = {"-e", "--env"}, description = "The environment from which to get the value.")
    String env;

    @CommandLine.Option(names = {"-k", "--key"}, description = "The name of the value of interest.")
    String key;

    @CommandLine.Option(names = {"-a", "--active-env"}, description = "Show the name of the active environment.")
    Boolean activeEnv;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    boolean help;

    public EvolvenCommandConfigGet(Command command) {
        super(command);
    }

    @Override
    public void execute() throws CommandException {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        addOption(env, this);
        addOption(key, this);
        addFlag(activeEnv, this);
        invokeHandler();
    }
}
