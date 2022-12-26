package com.evolven.cli.exception;

import com.evolven.logging.Logger;
import picocli.CommandLine;

public class ExecutionExceptionHandler implements CommandLine.IExecutionExceptionHandler {

    public static final int DEFAULT_RETURN_STATUS = 1;
    private Logger logger = new Logger(this);
    @Override
    public int handleExecutionException(Exception ex, CommandLine commandLine, CommandLine.ParseResult parseResult) throws Exception {
        String msg = ex.getMessage();
        int status = DEFAULT_RETURN_STATUS;
        if (ex instanceof EvolvenCommandException) status = ((EvolvenCommandException) ex).getStatus();
        if (!msg.isEmpty()) {
            logger.error(msg);
            System.err.println(msg);
        }
        logger.error("Exit status: " + status);
        return status;
    }
}
