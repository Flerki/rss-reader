package com.amairovi.cli.command;

import com.amairovi.Core;
import com.amairovi.cli.CommandProcessor;

public class ShowPropertyProcessor implements CommandProcessor {
    private static final String PATTERN = "Property with name=%s will be printed from now on for feed with id=%d.";

    private final Core core;

    public ShowPropertyProcessor(Core core) {
        this.core = core;
    }

    @Override
    public void process(String[] params) {
        String propertyName = params[1];
        int feedId = Integer.valueOf(params[2]);
        core.showProperty(feedId, propertyName);
        String message = String.format(PATTERN, propertyName, feedId);
        System.out.println(message);
    }
}
