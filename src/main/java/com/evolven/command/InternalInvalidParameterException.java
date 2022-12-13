package com.evolven.command;

import com.evolven.logging.Logger;

public class InternalInvalidParameterException extends RuntimeException {

    private Logger logger = new Logger(this);
    public InternalInvalidParameterException(String param) {
        super("Invalid parameter: " + param + ".");
        logger.error(getMessage());
    }
}
