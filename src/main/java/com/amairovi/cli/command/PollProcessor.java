package com.amairovi.cli.command;

import com.amairovi.Core;
import com.amairovi.cli.CommandProcessor;

public class PollProcessor implements CommandProcessor {
    private static final String ENABLE_PATTERN = "Feed with id=%d will be polled from now on.";
    private static final String DISABLE_PATTERN = "Feed with id=%d will not be polled from now on.";

    private final Core core;

    public PollProcessor(Core core) {
        this.core = core;
    }

    @Override
    public void process(String[] params) {
        String pollStatus = params[1];

        int id = Integer.valueOf(params[2]);

        if (pollStatus.equals("on")) {
            core.enablePoll(id);
            String message = String.format(ENABLE_PATTERN, id);
            System.out.println(message);
        }

        if (pollStatus.equals("off")) {
            core.disablePoll(id);
            String message = String.format(DISABLE_PATTERN, id);
            System.out.println(message);
        }
    }
}
