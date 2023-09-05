package com.evolven.cli.sub;

import com.evolven.cli.main.EvolvenCommandEnv;
import com.evolven.command.Command;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

@CommandLine.Command(name = EvolvenCommandPluginDelete.COMMAND_NAME, header = "List Evolven plugins")
public class EvolvenCommandPluginDelete extends EvolvenCommandEnv {

    public static final String COMMAND_NAME = "plugin-delete";

    @CommandLine.Option(names = {"-p", "--plugin-id"}, description = "Plugin ID.")
    protected Long id;

    public EvolvenCommandPluginDelete(Command command) {
        super(command);
    }

    @Override
    protected void addParameters() throws InvalidParameterException {
        addOption(id, this);
    }
}
