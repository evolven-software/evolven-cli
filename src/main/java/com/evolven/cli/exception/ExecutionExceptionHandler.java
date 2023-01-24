package com.evolven.cli.exception;

import com.evolven.logging.LoggerManager;
import picocli.CommandLine;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExecutionExceptionHandler implements CommandLine.IExecutionExceptionHandler {

    public static final int DEFAULT_RETURN_STATUS = 1;
    private Logger logger = LoggerManager.getLogger(this);
    @Override
    public int handleExecutionException(Exception ex, CommandLine commandLine, CommandLine.ParseResult parseResult) throws Exception {
        String msg = ex.getMessage();
        int status = DEFAULT_RETURN_STATUS;
        if (ex instanceof EvolvenCommandException) status = ((EvolvenCommandException) ex).getStatus();
        if (!msg.isEmpty()) {
            logger.log(Level.SEVERE, msg, ex);
            System.err.println(msg);
        }
        logger.log(Level.SEVERE, "Exit status: " + status);
        return status;
    }
}
