package com.evolven.command;


import com.evolven.logging.Logger;

public class InvalidParameterException extends CommandException {

    private Logger logger = new Logger(this);
    public InvalidParameterException(String msg) {
        super(msg);
        logger.error(msg);
    }

    public InvalidParameterException(String option, String invalidParameter, String details) {
        super("Invalid parameter value \"" + invalidParameter + "\" for the \"" + option + "\" option. " + details);
        logger.error(getMessage());
    }

    public InvalidParameterException(String invalidParameter, String details) {
        super("Invalid parameter value \"" + invalidParameter + "\". " + details);
        logger.error(getMessage());
    }
}
