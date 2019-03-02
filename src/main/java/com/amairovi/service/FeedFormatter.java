package com.amairovi.service;

import com.amairovi.model.Feed;
import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class FeedFormatter {

    private final StringBuilder str;
    private final Map<String, Boolean> entryParameterNameToVisibility;
    private final Feed feed;

    public FeedFormatter(Feed feed) {
        str = new StringBuilder();
        entryParameterNameToVisibility = feed.getEntryParameterNameToVisibility();
        this.feed = feed;
    }

    public String format(SyndFeed syndFeed) {
        String str = syndFeed.getEntries()
                .stream()
                .limit(feed.getAmountOfElementsAtOnce())
                .map(this::entryToString)
                .collect(joining(lineSeparator()));

        return addLineSeparatorsBeforeAndAfterIfNotEmpty(str);
    }

    private String entryToString(SyndEntry entry) {
        addParameterIfNecessary("title", () -> entry.getTitle() != null ? entry.getTitle() : "-");
        addParameterIfNecessary("author", () -> entry.getAuthor() != null ? entry.getAuthor() : "-");
        addParameterIfNecessary("comments", () -> entry.getComments() != null ? entry.getAuthor() : "-");
        addParameterIfNecessary("link", () -> entry.getLink() != null ? entry.getLink() : "-");
        addParameterIfNecessary("uri", () -> entry.getUri() != null ? entry.getUri() : "-");
        addParameterIfNecessary("description", () -> {
            String value = entry.getDescription().getValue();
            return value != null ? value : "-";
        });
        addParameterIfNecessary("publishedDate", () -> entry.getPublishedDate() != null ? entry.getPublishedDate().toString() : "-");
        addParameterIfNecessary("updateDate", () -> entry.getUpdatedDate() != null ? entry.getUpdatedDate().toString() : "-");
        addParameterIfNecessary("content", () -> contentToString(entry.getContents()));
        addParameterIfNecessary("categories", () -> categoriesToString(entry.getCategories()));

        entry.getForeignMarkup()
                .forEach(element -> addParameterIfNecessary(element.getName(), () -> element.getValue() != null ? element.getValue() : "-"));
        return str.toString();
    }

    private void addParameterIfNecessary(String propertyName, Supplier<String> getValue) {
        if (entryParameterNameToVisibility.containsKey(propertyName) && entryParameterNameToVisibility.get(propertyName)) {
            String value = getValue.get();
            str.append(propertyName)
                    .append(": ")
                    .append(value)
                    .append(lineSeparator());
        }
    }

    private String categoriesToString(List<SyndCategory> categories) {
        return categories.stream().map(SyndCategory::getName).collect(joining(", "));
    }

    private String contentToString(List<SyndContent> contents) {
        return contents.stream().map(SyndContent::getValue).collect(joining());
    }

    private String addLineSeparatorsBeforeAndAfterIfNotEmpty(String str) {
        if (str.length() == 0){
            return str;
        }
        return lineSeparator() + str + lineSeparator();
    }
}
