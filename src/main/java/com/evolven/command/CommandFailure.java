package com.evolven.command;

public class CommandFailure extends CommandException {

    public CommandFailure() {
        super("Command failed");
    }
}
