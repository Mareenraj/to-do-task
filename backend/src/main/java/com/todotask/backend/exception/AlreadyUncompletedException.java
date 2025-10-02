package com.todotask.backend.exception;

public class AlreadyUncompletedException extends RuntimeException {
    public AlreadyUncompletedException(String message) {
        super(message);
    }
}
