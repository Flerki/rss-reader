package com.amairovi.cli;

import java.util.Scanner;

public interface CommandProcessor {
    default void process(String[] params, Scanner scanner){
        throw new RuntimeException("not implemented");
    }
}
