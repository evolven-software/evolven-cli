package com.evolven.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerManager {
    private static LoggerManager instance = null;

    public static final String CONFIG_FILE_NAME = "evolven-logging.properties";
    File loggingDirectory;
    FileHandler fileHandler = null;

    private LoggerManager(File loggingDirectory) {
        this.loggingDirectory = loggingDirectory;
        File config = getConfigFile();
        LogManager.getLogManager().reset();
        if (config.exists()) {
            try {
                LogManager.getLogManager().readConfiguration(new FileInputStream(config.getCanonicalFile()));
            } catch (IOException e) {
                System.err.println("Failed to load logging configurations from " + config + "." );
            }
        }
        try {
            fileHandler = new FileHandler(new File(loggingDirectory, "evolven-cli%g.log").toString() , 8*1024*1024, 10, true );
            fileHandler.setFormatter(new Formatter());
        } catch (IOException e) {
            System.err.println("Failed to load file handler, with the file " + config + "." );
        }

    }

    public static LoggerManager getInstance() {
        if (instance == null) {
            System.err.println("LoggerManager is not initialized");
        }
        return instance;
    }

    public File getConfigFile() {
       return new File(loggingDirectory, CONFIG_FILE_NAME);
    }

    protected Logger initializeLogger(Logger logger) {
        logger.setLevel(Level.ALL);
        if (fileHandler != null) {
            logger.addHandler(fileHandler);
        }
        return logger;
    }

    public static LoggerManager createInstance(File loggingDirectory) {
        if (instance != null) {
            System.err.println("LoggerManager is initialized already");
        } else {
            instance = new LoggerManager(loggingDirectory);
        }
        return instance;
    }

    static public Logger getLogger(Object obj) {
        return getLogger(obj.getClass().getName());
    }

    static public Logger getLogger(Class c) {
        return getLogger(c.getName());
    }
    static public Logger getLogger(String name) {
        Logger logger = java.util.logging.Logger.getLogger(name);
        LoggerManager instance = LoggerManager.getInstance();
        if (instance != null) instance.initializeLogger(logger);
        return logger;
    }
}
