package com.lafarleaf.planner.utils.Exceptions;

public class EmailNotSentException extends RuntimeException {
    public EmailNotSentException(String msg) {
        super(msg);
    }

    public EmailNotSentException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
