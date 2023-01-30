package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import picocli.CommandLine;


@CommandLine.Command(name = EvolvenCommandConfigGet.COMMAND_NAME)
public class EvolvenCommandConfigGet extends EvolvenCommand {
    protected final static String COMMAND_NAME = "get";


    @CommandLine.Option(names = {"-e", "--env"}, description = "The environment from which to get the value.")
    protected String env;

    @CommandLine.Option(names = {"-k", "--key"}, description = "The name of the value of interest.")
    protected String key;

    @CommandLine.Option(names = {"-a", "--active-env"}, description = "Show the name of the active environment.")
    protected Boolean activeEnv;

    @CommandLine.Spec
    protected CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    protected boolean help;

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
