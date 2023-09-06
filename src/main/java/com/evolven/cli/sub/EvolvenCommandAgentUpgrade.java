package com.evolven.cli.sub;

import com.evolven.cli.main.EvolvenCommandEnv;
import com.evolven.command.Command;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

@CommandLine.Command(name = EvolvenCommandAgentUpgrade.COMMAND_NAME, header = "Upgrade Evolven Agent.")
public class EvolvenCommandAgentUpgrade extends EvolvenCommandEnv {

    public static final String COMMAND_NAME = "upgrade";

    @CommandLine.Option(names = {"-i", "--host-id"}, required = true, description = "The ID of the host on which the agent will be upgraded (find it in the UI)")
    protected String host;

    @CommandLine.Option(names = {"-v", "--version"}, required = true, description = "New Agent version (the crc of the version should be present on the server)")
    protected String version;

    public EvolvenCommandAgentUpgrade(Command command) {
        super(command);
    }

    @Override
    protected void addParameters() throws InvalidParameterException {
        addOption(host, this);
        addOption(version, this);
    }
}
