package com.terrideboer.bookbase.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException() {
        super("You are not allowed access to this resource");
    }
}
