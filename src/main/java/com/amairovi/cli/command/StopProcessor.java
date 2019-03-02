package com.amairovi.cli.command;

import com.amairovi.core.FeedServiceFacade;
import com.amairovi.cli.CommandProcessor;

public class StopProcessor implements CommandProcessor {
    private final FeedServiceFacade feedServiceFacade;

    public StopProcessor(FeedServiceFacade feedServiceFacade) {
        this.feedServiceFacade = feedServiceFacade;
    }

    @Override
    public void process(String[] params) {
        feedServiceFacade.stop();
        System.exit(0);
    }
}
