package com.amairovi.cli.command;

import com.amairovi.Core;
import com.amairovi.cli.CommandProcessor;
import com.amairovi.dto.FeedBriefInfo;

import java.util.List;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class ListProcessor implements CommandProcessor {
    private final static String PATTERN = "id: %d%n" +
            "name: %s%n" +
            "link: %s%n" +
            "filename: %s";

    private final Core core;

    public ListProcessor(Core core) {
        this.core = core;
    }

    @Override
    public void process(String[] params) {
        List<FeedBriefInfo> feeds = core.list();
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
        return String.format(PATTERN, id, name, link, filename);

    }
}
