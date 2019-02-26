package com.amairovi.service;

import com.amairovi.model.Feed;
import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FeedFormatter {

    private final static String PATTERN = "%s: %s%n";

    public String format(SyndFeed syndFeed, Feed feed) {
        Map<String, Boolean> entryParameterNameToVisibility = feed.getEntryParameterNameToVisibility();

        return syndFeed.getEntries()
                .stream()
                .limit(feed.getFeedExtras().getAmountOfElementsAtOnce())
                .map(entry -> entryToString(entry, entryParameterNameToVisibility))
                .collect(Collectors.joining());

    }

    private String entryToString(SyndEntry entry, Map<String, Boolean> entryParameterNameToVisibility) {
        StringBuilder str = new StringBuilder();

        str.append(String.format("%n"));

        if (entryParameterNameToVisibility.containsKey("content") && entryParameterNameToVisibility.get("content")) {

            String content = String.format(PATTERN, "content", contentToString(entry.getContents()));
            str.append(content);
        }

        if (entryParameterNameToVisibility.containsKey("categories") && entryParameterNameToVisibility.get("categories")) {
            String categories = String.format(PATTERN, "categories", categoriesToString(entry.getCategories()));
            str.append(categories);
        }

        entry.getForeignMarkup()
                .stream()
                .filter(element -> entryParameterNameToVisibility.containsKey(element.getName()))
                .filter(element -> entryParameterNameToVisibility.get(element.getName()))
                .map(element -> String.format(PATTERN, element.getName(), element.getValue()))
                .forEach(str::append);
        return str.toString();
    }

    private String categoriesToString(List<SyndCategory> categories) {
        return categories.stream().map(SyndCategory::getName).collect(Collectors.joining(", "));
    }

    private String contentToString(List<SyndContent> contents) {
        return contents.stream().map(SyndContent::getValue).collect(Collectors.joining());
    }
}
