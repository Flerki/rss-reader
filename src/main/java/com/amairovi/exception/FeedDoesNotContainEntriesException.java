package com.amairovi.exception;

public class FeedDoesNotContainEntriesException extends RuntimeException {
    public FeedDoesNotContainEntriesException() {
        super("Feed doesn't contain entries");
    }
}
