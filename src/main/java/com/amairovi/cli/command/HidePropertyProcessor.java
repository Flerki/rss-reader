package com.amairovi.cli.command;

import com.amairovi.core.FeedServiceFacade;
import com.amairovi.cli.CommandProcessor;

public class HidePropertyProcessor implements CommandProcessor {
    private static final String PATTERN = "Property with name=%s will not be printed from now on for feed with id=%d.";

    private final FeedServiceFacade feedServiceFacade;

    public HidePropertyProcessor(FeedServiceFacade feedServiceFacade) {
        this.feedServiceFacade = feedServiceFacade;
    }

    @Override
    public void process(String[] params) {
        String propertyName = params[1];
        int feedId = Integer.valueOf(params[2]);
        feedServiceFacade.hideProperty(feedId, propertyName);
        String message = String.format(PATTERN, propertyName, feedId);
        System.out.println(message);
    }
}
