package com.evolven.cli.sub;

import com.evolven.cli.main.EvolvenCommandEnv;
import com.evolven.command.Command;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

@CommandLine.Command(name = EvolvenCommandPluginList.COMMAND_NAME, header = "List Evolven plugins")
public class EvolvenCommandPluginList extends EvolvenCommandEnv {

    public static final String COMMAND_NAME = "plugin-list";

    public EvolvenCommandPluginList(Command command) {
        super(command);
    }

    @Override
    protected void addParameters() throws InvalidParameterException {
    }
}
