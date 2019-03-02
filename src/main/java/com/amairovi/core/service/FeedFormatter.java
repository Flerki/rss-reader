package com.amairovi.core.service;

import com.amairovi.core.model.Feed;
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
        syndFeed.getEntries()
                .stream()
                .limit(feed.getAmountOfElementsAtOnce())
                .forEach(this::entryToString);

        return addLineSeparatorsBeforeAndAfterIfNotEmpty();
    }

    private void entryToString(SyndEntry entry) {
        addParameterIfNecessary("title", () -> entry.getTitle() != null ? entry.getTitle() : "-");
        addParameterIfNecessary("author", () -> entry.getAuthor() != null ? entry.getAuthor() : "-");
        addParameterIfNecessary("comments", () -> entry.getComments() != null ? entry.getAuthor() : "-");
        addParameterIfNecessary("link", () -> entry.getLink() != null ? entry.getLink() : "-");
        addParameterIfNecessary("uri", () -> entry.getUri() != null ? entry.getUri() : "-");
        addParameterIfNecessary("description", () -> {
            SyndContent description = entry.getDescription();
            String value =  description != null ? description.getValue() : null;
            return value != null ? value : "-";
        });
        addParameterIfNecessary("publishedDate", () -> entry.getPublishedDate() != null ? entry.getPublishedDate().toString() : "-");
        addParameterIfNecessary("updateDate", () -> entry.getUpdatedDate() != null ? entry.getUpdatedDate().toString() : "-");
        addParameterIfNecessary("content", () -> contentToString(entry.getContents()));
        addParameterIfNecessary("categories", () -> categoriesToString(entry.getCategories()));

        entry.getForeignMarkup()
                .forEach(element -> addParameterIfNecessary(element.getName(), () -> element.getValue() != null ? element.getValue() : "-"));
        str.append(lineSeparator());
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

    private String addLineSeparatorsBeforeAndAfterIfNotEmpty() {
        if (str.length() == 0){
            return "";
        }
        return lineSeparator() + str + lineSeparator();
    }
}
