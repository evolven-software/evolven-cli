package com.evolven.command;

import com.evolven.common.ExceptionUtils;
import com.evolven.common.StringUtils;
import com.evolven.logging.LoggerManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandException extends Exception {
    private Logger logger = LoggerManager.getLogger(this);

    private String msg;

    public CommandException(String msg) {
        super(msg);
        this.msg = msg;
        logger.log(Level.SEVERE, msg + "\n" + ExceptionUtils.getStackTrace(this));
    }

    public CommandException(String msg, Exception e) {
        super(msg + "\n" + e.getMessage());
        this.msg = msg;
        logger.log(Level.SEVERE, msg + "\n" + ExceptionUtils.getStackTrace(this));
    }

    public CommandException(Exception e) {
        super(e);
        logger.log(Level.SEVERE, ExceptionUtils.getStackTrace(this));
    }

    @Override
    public String getMessage() {
        if (StringUtils.isNullOrBlank(msg)) return super.getMessage();
        return msg;
    }
}
