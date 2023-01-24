package com.evolven.command;


import com.evolven.logging.LoggerManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class InternalInvalidParameterException extends RuntimeException {

    private Logger logger = LoggerManager.getLogger(this);
    public InternalInvalidParameterException(String param) {
        super("Invalid parameter: " + param + ".");
        logger.log(Level.SEVERE, "Invalid parameter: " + param + ".", this);
    }
}
