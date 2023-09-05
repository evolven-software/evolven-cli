package com.evolven.cli.sub;

import com.evolven.cli.main.EvolvenCommandEnv;
import com.evolven.command.Command;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;


@CommandLine.Command(name = EvolvenCommandConfigGet.COMMAND_NAME)
public class EvolvenCommandConfigGet extends EvolvenCommandEnv {

    public static final String COMMAND_NAME = "get";

    @CommandLine.Option(names = {"-k", "--key"}, description = "The name of the value of interest.")
    protected String key;

    @CommandLine.Option(names = {"-a", "--active-env"}, description = "Show the name of the active environment.")
    protected Boolean activeEnv;

    public EvolvenCommandConfigGet(Command command) {
        super(command);
    }

    @Override
    protected void addParameters() throws InvalidParameterException {
        addOption(key, this);
        addFlag(activeEnv, this);
    }
}
