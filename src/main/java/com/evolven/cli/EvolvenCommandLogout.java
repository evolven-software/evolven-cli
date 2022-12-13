package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;
import picocli.CommandLine;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@CommandLine.Command(name = "logout", header = "Invalidate Evolven credentials")
public class EvolvenCommandLogout extends EvolvenCommand implements Runnable {

    @CommandLine.Option(names = {"-e", "--env"}, defaultValue = "test", description = "Environment label.")
    String env;

    public EvolvenCommandLogout(Command command) {
        super(command);
    }

    @CommandLine.Option(names = {"-h", "--help"}, description = "Show help")
    boolean help;

    @Spec
    CommandSpec spec;

    @Override
    public void run() {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        try {
            addOption(env, this);
            execute();
        } catch (CommandException e) {
            throw new EvolvenCommandException(e);
        }
    }
}
