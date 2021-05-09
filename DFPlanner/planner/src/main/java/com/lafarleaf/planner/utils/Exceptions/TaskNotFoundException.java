package com.lafarleaf.planner.utils.Exceptions;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String msg) {
        super(msg);
    }

    public TaskNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
