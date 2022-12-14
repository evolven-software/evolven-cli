package com.evolven.cli;

public class EvolvenCommandException extends RuntimeException {

    public EvolvenCommandException(String msg) {
        super(msg);
    }

    public EvolvenCommandException(Exception e) {
        super(e);
    }
}
