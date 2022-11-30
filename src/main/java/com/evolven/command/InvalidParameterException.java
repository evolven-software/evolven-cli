package com.evolven.command;


public class InvalidParameterException extends CommandException {
    public InvalidParameterException(String msg) {
        super(msg);
    }

    public InvalidParameterException(String option, String invalidParameter, String details) {
        super("Invalid parameter value \"" + invalidParameter + "\" for the \"" + option + "\" option. " + details);
    }

    public InvalidParameterException(String invalidParameter, String details) {
        super("Invalid parameter value \"" + invalidParameter + "\". " + details);
    }
}
