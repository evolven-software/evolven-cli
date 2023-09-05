package com.evolven.cli.single;

import com.evolven.cli.main.EvolvenCommandEnv;
import com.evolven.command.Command;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;

@CommandLine.Command(name = EvolvenCommandSearch.COMMAND_NAME, header = "Search Evolven entity.")
public class EvolvenCommandSearch extends EvolvenCommandEnv {

    public static final String COMMAND_NAME = "search";

    @CommandLine.Option(names = {"-q", "--query"}, required = true, description = "Evolven search query.")
    protected String query;

    public EvolvenCommandSearch(Command command) {
        super(command);
    }

    @Override
    protected void addParameters() throws InvalidParameterException {
        addOption(query, this);
    }
}
