package com.evolven.command;

import com.evolven.common.StringUtils;
import com.evolven.logging.Logger;

public class CommandException extends Exception {
    private Logger logger = new Logger(this);

    private String msg;

    public CommandException(String msg) {
        super(msg);
        this.msg = msg;
        logger.error(msg);
    }

    public CommandException(String msg, Exception e) {
        super(msg + "\n" + e.getMessage());
        this.msg = msg;
        logger.error(msg + "\n" + msg);
    }

    public CommandException(Exception e) {
        super(e);
        logger.error(e.getMessage());
    }

    @Override
    public String getMessage() {
        if (StringUtils.isNullOrBlank(msg)) return super.getMessage();
        return msg;
    }
}
