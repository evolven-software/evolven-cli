package com.evolven.command;

import com.evolven.logging.Logger;

public class CommandException extends Exception {
    private Logger logger = new Logger(this);

    public CommandException(String msg) {
        super(msg);
        logger.error(msg);
    }
}
