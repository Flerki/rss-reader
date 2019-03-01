package com.amairovi.cli.command;

import com.amairovi.Core;
import com.amairovi.cli.CommandProcessor;
import com.amairovi.dto.FeedInfo;

public class CreateProcessor implements CommandProcessor {
    private final Core core;

    public CreateProcessor(Core core) {
        this.core = core;
    }

    @Override
    public void process(String[] params) {
        System.out.println("Initialising feed...");

        String url = params[1];
        int feedId = initialiseFeed(url);

        System.out.println("Feed is initialised");

        printFeedInfo(feedId);
    }

    private void printFeedInfo(int feedId) {
        FeedInfo feedFullDescription = core.getFeedFullDescription(feedId);

        System.out.println("Feed configs");
        System.out.println("id: " + feedFullDescription.getId());
        System.out.println("name: " + feedFullDescription.getName());
        System.out.println("link: " + feedFullDescription.getLink());
        System.out.println("filename: " + feedFullDescription.getFilename());
        System.out.println("amount of elements polled at once: " + feedFullDescription.getAmountOfElementsAtOnce());
        System.out.println("poll period: " + feedFullDescription.getPollPeriodInMs());
    }

    private int initialiseFeed(String url) {
        return core.createFeed(url);
    }
}
