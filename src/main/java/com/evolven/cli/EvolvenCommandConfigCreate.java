package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.logging.LoggerManager;
import picocli.CommandLine;

import com.evolven.command.CommandException;

import java.util.logging.Logger;

@CommandLine.Command(
        name = "create",
        header = "Creates configuration files in the current working directory " +
        "(use it if other methods filed and the the program assumed to be executed from the current location always")
public class EvolvenCommandConfigCreate extends EvolvenCommand {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Print help.")
    private boolean help;

    private Logger logger = LoggerManager.getLogger(this);

    @CommandLine.Option(names = {"-f", "--force"}, description = "Override the config directory if exists.")
    private Boolean force = false;

    public EvolvenCommandConfigCreate(Command command) {
        super(command);
    }

    @Override
    public void execute() throws CommandException {
        if (help) {
            spec.commandLine().usage(System.out);
            return;
        }
        addFlag(force, this);
        invokeHandler();
    }
}
