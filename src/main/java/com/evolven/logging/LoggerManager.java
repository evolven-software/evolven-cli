package com.evolven.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.*;

public class LoggerManager {
    private static LoggerManager instance = null;

    public static final String CONFIG_FILE_NAME = "evolven-logging.properties";
    File loggingDirectory;
    FileHandler infoFileHandler = null;
    FileHandler debugFileHandler = null;

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
            infoFileHandler = new FileHandler(new File(loggingDirectory, "evolven-cli-info_%g.log").toString() , 8*1024*1024, 10, true );
            debugFileHandler = new FileHandler(new File(loggingDirectory, "evolven-cli-debug_%g.log").toString() , 8*1024*1024, 10, true );
            infoFileHandler.setFormatter(new Formatter());
            debugFileHandler.setFormatter(new Formatter());
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
        logger.info.setLevel(Level.ALL);
        logger.debug.setLevel(Level.ALL);
        if (infoFileHandler != null) {
            logger.info.addHandler(infoFileHandler);
        }
        if (debugFileHandler != null) {
            logger.debug.addHandler(debugFileHandler);
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
}
