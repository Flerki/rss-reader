package com.amairovi.cli.command;

import com.amairovi.Core;
import com.amairovi.cli.CommandProcessor;
import com.amairovi.dto.ChannelInfo;
import com.amairovi.dto.FeedInfo;

import java.util.Scanner;

public class CreateProcessor implements CommandProcessor {
    private final Core core;

    public CreateProcessor(Core core) {
        this.core = core;
    }

    @Override
    public void process(String[] params, Scanner scanner) {
        System.out.println("Initialising feed...");

        String url = params[1];
        int feedId = initialiseFeed(url);

        System.out.println("Feed is initialised");
        System.out.println();

        printFeedInfo(feedId, scanner);
    }

    private void printFeedInfo(int feedId, Scanner scanner) {
        FeedInfo feedFullDescription = core.getFeedFullDescription(feedId);

        System.out.println("Feed configs");
        System.out.println("id: " + feedFullDescription.getId());
        System.out.println("name: " + feedFullDescription.getName());
        System.out.println("link: " + feedFullDescription.getLink());
        System.out.println("filename: " + feedFullDescription.getFilename());
        System.out.println("amount of elements polled at once: " + feedFullDescription.getAmountOfElementsAtOnce());
        System.out.println("poll period: " + feedFullDescription.getPollPeriodInMs());
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
        if (readYesNo(scanner)) {
            for (String propertyName : feedFullDescription.getEntryParameterNameToVisibility()
                    .keySet()) {
                System.out.println("Do you want to hide " + propertyName + "? (y/n)");
                if (readYesNo(scanner)) {
                    core.hideProperty(feedId, propertyName);
                }
            }
        }
    }

    private boolean readYesNo(Scanner scanner) {
        while (true) {
            String line = scanner.nextLine();
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
        return core.createFeed(url);
    }
}
