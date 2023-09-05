package com.evolven.cli.single;

import com.evolven.cli.main.EvolvenCommandEnv;
import com.evolven.command.Command;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

@CommandLine.Command(name = EvolvenCommandLogout.COMMAND_NAME, header = "Invalidate Evolven credentials")
public class EvolvenCommandLogout extends EvolvenCommandEnv {

    public static final String COMMAND_NAME = "logout";

    public EvolvenCommandLogout(Command command) {
        super(command);
    }
    
    @Override
    protected void addParameters() throws InvalidParameterException {}
}
