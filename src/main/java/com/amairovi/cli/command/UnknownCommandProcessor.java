package com.amairovi.cli.command;

import com.amairovi.cli.CommandProcessor;

public class UnknownCommandProcessor implements CommandProcessor {
    @Override
    public void process(String[] params) {
        System.out.println("Unknown command");
    }
}
