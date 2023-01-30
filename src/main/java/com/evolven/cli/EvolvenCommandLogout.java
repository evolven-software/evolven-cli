package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@CommandLine.Command(name = "logout", header = "Invalidate Evolven credentials")
public class EvolvenCommandLogout extends EvolvenCommand {

    @CommandLine.Option(names = {"-e", "--env"}, defaultValue = "test", description = "Environment label.")
    protected String env;

    public EvolvenCommandLogout(Command command) {
        super(command);
    }

    @CommandLine.Option(names = {"-h", "--help"}, description = "Show help")
    protected boolean help;

    @Spec
    protected CommandSpec spec;

    @Override
    public void execute() throws CommandException {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        addOption(env, this);
        invokeHandler();
    }
}
