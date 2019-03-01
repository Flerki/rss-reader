package com.amairovi.cli;

public interface CommandProcessor {
    default void process(String[] params){
        throw new RuntimeException("not implemented");
    }
}
