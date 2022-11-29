package com.evolven.command;


public class InvalidParameterException extends Exception {
    public InvalidParameterException(String msg) {
        super(msg);
    }

    public InvalidParameterException(String option, String invalidParameter, String explanation) {
        super(option);
    }

    public InvalidParameterException(String invalidParameter, String explanation) {
        super(invalidParameter);
    }
}
