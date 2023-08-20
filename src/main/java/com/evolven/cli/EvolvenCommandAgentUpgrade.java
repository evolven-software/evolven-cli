package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(name = "upgrade", header = "Upgrade Evolven Agent.")
public class EvolvenCommandAgentUpgrade extends EvolvenCommand {

    @CommandLine.Option(names = {"-i", "--host-id"}, required = true, description = "The ID of the host on which the agent will be upgraded (find it in the UI)")
    protected String host;

    @CommandLine.Option(names = {"-v", "--version"}, required = true, description = "New Agent version (the crc of the version should be present on the server)")
    protected String version;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    protected boolean help;

    @CommandLine.Spec
    protected CommandLine.Model.CommandSpec spec;
    public EvolvenCommandAgentUpgrade(Command command) {
        super(command);
    }

    @Override
    public void execute() throws CommandException {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        addOption(host, this);
        addOption(version, this);
        invokeHandler();
    }
}
