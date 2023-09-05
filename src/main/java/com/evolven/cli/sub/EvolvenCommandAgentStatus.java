package com.evolven.cli.sub;

import com.evolven.cli.main.EvolvenCommandEnv;
import com.evolven.command.Command;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

@CommandLine.Command(name = EvolvenCommandAgentStatus.COMMAND_NAME, header = "Get Evolven Agent status.")
public class EvolvenCommandAgentStatus extends EvolvenCommandEnv {

    public static final String COMMAND_NAME = "upgrade";

    @CommandLine.Option(names = {"-i", "--host-id"}, required = true, description = "The ID of the agent host (find it in the UI)")
    protected String host;

    public EvolvenCommandAgentStatus(Command command) {
        super(command);
    }

    @Override
    protected void addParameters() throws InvalidParameterException {
        addOption(host, this);
    }
}
