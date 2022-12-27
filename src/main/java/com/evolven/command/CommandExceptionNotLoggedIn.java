package com.evolven.command;

public class CommandExceptionNotLoggedIn extends CommandException {

    public CommandExceptionNotLoggedIn() {
        super("Login required");
    }
}
