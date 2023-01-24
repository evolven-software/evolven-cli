package com.evolven.command;


import com.evolven.logging.LoggerManager;

import java.util.logging.Logger;

public class InvalidParameterException extends CommandException {

    private Logger logger = LoggerManager.getLogger(this);
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
