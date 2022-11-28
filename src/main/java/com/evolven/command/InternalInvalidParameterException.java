package com.evolven.command;

public class InternalInvalidParameterException extends RuntimeException {
    public InternalInvalidParameterException(String param) {
        super("Invalid parameter: " + param);
    }
}
