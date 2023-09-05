package com.evolven.cli.sub;

import com.evolven.cli.main.EvolvenCommandEnv;
import com.evolven.command.Command;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

@CommandLine.Command(name = EvolvenCommandConfigSet.COMMAND_NAME)
public class EvolvenCommandConfigSet extends EvolvenCommandEnv {

    public static final String COMMAND_NAME = "set";

    @CommandLine.Option(names = {"-a", "--active-env"}, description = "Set active environment.")
    protected String activeEnv;

    @CommandLine.Option(names = {"-k", "--key"}, description = "The key to be set.")
    protected String key;

    @CommandLine.Option(names = {"-v", "--value"}, description = "The value to be set.")
    protected String value;

    public EvolvenCommandConfigSet(Command command) {
        super(command);
    }

    @Override
    protected void addParameters() throws InvalidParameterException {
        addOption(activeEnv, this);
        addOption(key, this);
        addOption(value, this);
    }
}
