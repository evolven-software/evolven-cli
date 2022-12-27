package com.evolven.cli.exception;

public class EvolvenCommandException extends RuntimeException {

    String msg;
    int status = ExecutionExceptionHandler.DEFAULT_RETURN_STATUS;

    public EvolvenCommandException() {
        super("");
    }
    public EvolvenCommandException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public EvolvenCommandException(String msg, int status) {
        this(msg);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        if (msg == null) return super.getMessage();
        return msg;
    }

    public EvolvenCommandException(Exception e) {
        super(e);
        msg = e.getMessage();
    }
}
