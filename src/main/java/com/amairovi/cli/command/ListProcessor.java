package com.amairovi.cli.command;

import com.amairovi.core.FeedServiceFacade;
import com.amairovi.cli.CommandProcessor;
import com.amairovi.core.dto.FeedBriefInfo;

import java.util.List;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class ListProcessor implements CommandProcessor {
    private final static String PATTERN = "id: %d%n" +
            "name: %s%n" +
            "link: %s%n" +
            "filename: %s%n" +
            "is polled: %s";

    private final FeedServiceFacade feedServiceFacade;

    public ListProcessor(FeedServiceFacade feedServiceFacade) {
        this.feedServiceFacade = feedServiceFacade;
    }

    @Override
    public void process(String[] params) {
        List<FeedBriefInfo> feeds = feedServiceFacade.list();
        if (feeds.isEmpty()) {
            System.out.println("There is no feed.");
        } else {
            String listOfFeeds = feeds.stream()
                    .map(this::convertBriefFeedInfoToString)
                    .collect(joining(lineSeparator() + lineSeparator()));
            System.out.println(listOfFeeds);
        }
    }

    private String convertBriefFeedInfoToString(FeedBriefInfo feedBriefInfo) {
        int id = feedBriefInfo.getId();
        String name = feedBriefInfo.getName();
        String link = feedBriefInfo.getLink();
        String filename = feedBriefInfo.getFilename();
        String isPolled = feedBriefInfo.isPolled() ? "yes" : "no";
        return String.format(PATTERN, id, name, link, filename, isPolled);

    }

    @Override
    public String commandFormat() {
        return "list";
    }
}
