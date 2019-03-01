package com.amairovi.cli.command;

import com.amairovi.Core;
import com.amairovi.cli.CommandProcessor;

public class StopProcessor implements CommandProcessor {
    private final Core core;

    public StopProcessor(Core core) {
        this.core = core;
    }

    @Override
    public void process(String[] params) {
        core.stop();
        System.exit(0);
    }
}
