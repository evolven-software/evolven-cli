package com.evolven.command;

import com.evolven.logging.Logger;

public class CommandException extends Exception {
    private Logger logger = new Logger(this);

    public CommandException(String msg) {
        super(msg);
        logger.error(msg);
    }

    public CommandException(String msg, Exception e) {
        super(msg + "\n" + msg);
        logger.error(msg + "\n" + msg);
    }

    public CommandException(Exception e) {
        super(e);
        logger.error(e.getMessage());
    }
}
