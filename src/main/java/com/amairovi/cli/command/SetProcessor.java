package com.amairovi.cli.command;

import com.amairovi.Core;
import com.amairovi.cli.CommandProcessor;

public class SetProcessor implements CommandProcessor {

    private final Core core;

    public SetProcessor(Core core) {
        this.core = core;
    }

    @Override
    public void process(String[] params) {
        if (params.length != 4) {
            throwUnknownCommandException(params);
        }

        String property = params[1];

        if (property.equals("poll-period")) {
            long pollPeriodInSec = Integer.valueOf(params[2]);
            int id = Integer.valueOf(params[3]);
            core.setPollPeriodInMs(id, pollPeriodInSec * 1000);
            return;
        }

        if (property.equals("filename")) {
            String filename = params[2];
            int id = Integer.valueOf(params[3]);
            core.setFeedFilename(id, filename);
            return;
        }

        if (property.equals("item-amount")) {
            int itemAmount = Integer.valueOf(params[2]);
            int id = Integer.valueOf(params[3]);
            core.setFeedAmountOfElementsAtOnce(id, itemAmount);
            return;
        }

        throwUnknownCommandException(params);
    }

    @Override
    public String commandFormat() {
        return "set [poll-period|filename|item-amount] <poll-period-in-sec|filename|item-amount> <feedId>";
    }
}

