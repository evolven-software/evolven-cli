package com.evolven.logging;

import java.util.logging.*;

public class Logger {
    protected java.util.logging.Logger info;
    protected java.util.logging.Logger debug;

    public Logger(Object obj) {
        this(obj.getClass().getName());
    }

    public Logger(String name) {
        info = java.util.logging.Logger.getLogger(name);
        debug = java.util.logging.Logger.getLogger(name);
        LoggerManager instance = LoggerManager.getInstance();
        if (instance != null) instance.initializeLogger(this);
    }

    public void info(String info) {
        LogRecord lr = new LogRecord(Level.INFO, info);
        lr.setLoggerName(this.info.getName());
        this.info.log(lr);
        this.debug.log(lr);
    }

    public void debug(String info) {
        LogRecord lr = new LogRecord(Level.FINE, info);
        lr.setLoggerName(this.debug.getName());
        this.debug.log(lr);
    }

    public void error(String error) {
        LogRecord lr = new LogRecord(Level.SEVERE, error);
        lr.setLoggerName(this.debug.getName());
        this.info.log(lr);
        this.debug.log(lr);
    }

}
