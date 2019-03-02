package com.amairovi.cli.command;

import com.amairovi.core.FeedServiceFacade;
import com.amairovi.cli.CommandProcessor;
import com.amairovi.cli.formatter.FeedConfigsFormatter;
import com.amairovi.core.dto.ChannelInfo;
import com.amairovi.core.dto.FeedInfo;

import java.util.Scanner;

public class CreateProcessor implements CommandProcessor {
    private final FeedServiceFacade feedServiceFacade;
    private final FeedConfigsFormatter feedConfigsFormatter;
    private final Scanner input;

    public CreateProcessor(FeedServiceFacade feedServiceFacade, FeedConfigsFormatter feedConfigsFormatter, Scanner input) {
        this.feedServiceFacade = feedServiceFacade;
        this.feedConfigsFormatter = feedConfigsFormatter;
        this.input = input;
    }

    @Override
    public void process(String[] params) {
        System.out.println("Initialising feed...");

        String url = params[1];
        int feedId = initialiseFeed(url);

        System.out.println("Feed is initialised");
        System.out.println();

        printFeedInfo(feedId);
    }

    private void printFeedInfo(int feedId) {
        FeedInfo feedFullDescription = feedServiceFacade.getFeedFullDescription(feedId);

        System.out.println(feedConfigsFormatter.format(feedFullDescription));
        System.out.println();

        System.out.println("Feed info");
        ChannelInfo channelInfo = feedFullDescription.getChannelInfo();
        System.out.println("title: " + channelInfo.getTitle());
        System.out.println("link: " + channelInfo.getLink());
        System.out.println("description: " + channelInfo.getDescription());
        System.out.println("uri: " + channelInfo.getUri());
        System.out.println("generator: " + channelInfo.getGenerator());
        System.out.println("language: " + channelInfo.getLanguage());
        System.out.println();

        System.out.println("Feed's entries' properties");
        feedFullDescription.getEntryParameterNameToVisibility()
                .keySet()
                .forEach(System.out::println);

        System.out.println();

        System.out.println("Do you want to hide any of parameters? (y/n)");
        if (readYesNo()) {
            for (String propertyName : feedFullDescription.getEntryParameterNameToVisibility()
                    .keySet()) {
                System.out.println("Do you want to hide " + propertyName + "? (y/n)");
                if (readYesNo()) {
                    feedServiceFacade.hideProperty(feedId, propertyName);
                }
            }
        }
    }

    private boolean readYesNo() {
        while (true) {
            String line = input.nextLine();
            if (line.equals("y")) {
                return true;
            }
            if (line.equals("n")) {
                return false;
            }
            System.out.println("Please, enter 'y' or 'n'.");
        }
    }

    private int initialiseFeed(String url) {
        return feedServiceFacade.createFeed(url);
    }

    @Override
    public String commandFormat() {
        return "create <url>";
    }
}
