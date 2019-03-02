package com.amairovi.cli;

import com.amairovi.exception.UnknownCommandException;

import static java.lang.String.join;

public interface CommandProcessor {

    default void process(String[] params) {
        throw new RuntimeException("not implemented");
    }

    default void throwUnknownCommandException(String[] params) {
        String actual = join(" ", params);

        throw new UnknownCommandException(actual, commandFormat());
    }

    default String commandFormat(){
        return "Not implemented";
    }
}
