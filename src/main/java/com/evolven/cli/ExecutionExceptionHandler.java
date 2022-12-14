package com.evolven.cli;

import com.evolven.logging.Logger;
import picocli.CommandLine;

public class ExecutionExceptionHandler implements CommandLine.IExecutionExceptionHandler {
    private Logger logger = new Logger(this);
    @Override
    public int handleExecutionException(Exception ex, CommandLine commandLine, CommandLine.ParseResult parseResult) throws Exception {
        logger.error(ex.getMessage());
        System.err.println(ex.getMessage());
        return 2;
    }
}
