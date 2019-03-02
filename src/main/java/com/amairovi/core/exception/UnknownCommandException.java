package com.amairovi.core.exception;

public class UnknownCommandException extends RuntimeException{
    private final static String PATTERN = "Unknown command: %s%n" +
            "Right format: %s%n";

    private final String message;

    public UnknownCommandException(String actualCommand, String rightFormatDescription) {
        message = String.format(PATTERN, actualCommand, rightFormatDescription);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
