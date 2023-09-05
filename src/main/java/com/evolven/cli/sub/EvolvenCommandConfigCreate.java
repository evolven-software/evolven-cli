package com.evolven.cli.sub;

import com.evolven.cli.main.EvolvenCommandBase;
import com.evolven.command.Command;
import com.evolven.command.InvalidParameterException;
import com.evolven.logging.LoggerManager;
import picocli.CommandLine;

import java.util.logging.Logger;

@CommandLine.Command(
        name = EvolvenCommandConfigCreate.COMMAND_NAME,
        header = "Creates configuration files in the current working directory " +
                "(use it if other methods filed and the the program assumed to be executed from the current location always")
public class EvolvenCommandConfigCreate extends EvolvenCommandBase {

    public static final String COMMAND_NAME = "create";

    @CommandLine.Option(names = {"-f", "--force"}, description = "Override the config directory if exists.")
    protected Boolean force = false;

    public EvolvenCommandConfigCreate(Command command) {
        super(command);
    }

    @Override
    protected void addParameters() throws InvalidParameterException {
        addFlag(force, this);
    }
}
