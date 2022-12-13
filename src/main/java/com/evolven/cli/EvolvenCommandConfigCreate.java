package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.logging.Logger;
import picocli.CommandLine;

import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;
import com.evolven.command.InternalInvalidParameterException;

@CommandLine.Command(
        name = "create",
        header = "Creates configuration files in the current working directory " +
        "(use it if other methods filed and the the program assumed to be executed from the current location always")
public class EvolvenCommandConfigCreate extends EvolvenCommand implements Runnable {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    boolean help;

    private Logger logger = new Logger(this);

    @CommandLine.Option(names = {"-f", "--force"}, description = "Override the config directory if exists.")
    Boolean force = false;

    public EvolvenCommandConfigCreate(Command command) {
        super(command);
    }

    @Override
    public void run() {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        try {
            addFlag(force, this);
            execute();
        } catch(CommandException e) {
            throw new EvolvenCommandException(e);
        }
    }
}
