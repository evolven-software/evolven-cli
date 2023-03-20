package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@CommandLine.Command(name = "search", header = "Search Evolven entity.")
public class EvolvenCommandSearch extends EvolvenCommand {

    @CommandLine.Option(names = {"-q", "--query"}, required = true, description = "Evolven search query.")
    protected String query;

    @Spec
    protected CommandSpec spec;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    protected boolean help;

    public EvolvenCommandSearch(Command command) {
        super(command);
    }

    @Override
    public void execute() throws CommandException {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        addOption(query, this);
        invokeHandler();
    }
}
