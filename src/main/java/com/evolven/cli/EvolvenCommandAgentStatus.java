package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import picocli.CommandLine;

@CommandLine.Command(name = "upgrade", header = "Get Evolven Agent status.")
public class EvolvenCommandAgentStatus extends EvolvenCommand {

    @CommandLine.Option(names = {"-i", "--host-id"}, required = true, description = "The ID of the agent host (find it in the UI)")
    protected String host;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    protected boolean help;

    @CommandLine.Spec
    protected CommandLine.Model.CommandSpec spec;
    public EvolvenCommandAgentStatus(Command command) {
        super(command);
    }

    @Override
    public void execute() throws CommandException {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        addOption(host, this);
        invokeHandler();
    }
}
