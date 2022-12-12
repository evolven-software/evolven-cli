package com.evolven.cli;

import com.evolven.filesystem.command.LogoutCommand;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

@CommandLine.Command(name = "search", header = "Search Evolven entity.")
public class EvolvenCommandSearch extends EvolvenCommand implements Runnable {

    @CommandLine.Option(names = {"-q", "--query"}, required = true, description = "Evolven search query.")
    String query;

    @Spec
    CommandSpec spec;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")

    boolean help;

    public EvolvenCommandSearch(Command command) {
        super(command);

    }

    @Override
    public void run() {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        try {
            addOption(query, this);
            execute();

        } catch (InvalidParameterException e) {
            throw new RuntimeException(e);
        } catch (CommandException e) {
            throw new RuntimeException(e);
        }
    }
}
