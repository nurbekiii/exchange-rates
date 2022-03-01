package com.marcura.exhangerates.exception;

public class InvalidParameterException extends Exception {

    public InvalidParameterException(String msg) {
        super(msg);
    }

    public InvalidParameterException(String msg, Exception e) {
        super(msg + " because of " + e.toString());
    }
}