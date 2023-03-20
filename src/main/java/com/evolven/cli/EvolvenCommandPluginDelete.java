package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import picocli.CommandLine;

@CommandLine.Command(name = "plugin-delete", header = "List Evolven plugins")
public class EvolvenCommandPluginDelete extends EvolvenCommand {

    @CommandLine.Option(names = {"-p", "--plugin-id"}, description = "Plugin ID.")
    protected Long id;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    protected boolean help;

    @CommandLine.Spec
    protected CommandLine.Model.CommandSpec spec;

    public EvolvenCommandPluginDelete(Command command) {
        super(command);
    }

    @Override
    public void execute() throws CommandException {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        addOption(id, this);
        invokeHandler();
    }
}
