package com.amairovi.cli.formatter;

import com.amairovi.core.dto.Author;
import com.amairovi.core.dto.ChannelInfo;

import java.util.List;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class ChannelInfoFormatter extends AbstractFormatter {
    private final static String PATTERN =
            "Channel info%n" +
                    "title: %s%n" +
                    "authors: %s%n" +
                    "categories: %s%n" +
                    "descriptor: %s%n" +
                    "generator: %s%n" +
                    "link: %s%n" +
                    "language: %s%n" +
                    "uri: %s%n" +
                    "publishing date: %s%n";

    public String format(ChannelInfo channelInfo) {
        return String.format(PATTERN,
                ifNullThenUsePlaceholder(channelInfo.getTitle()),
                formatAuthors(channelInfo.getAuthors()),
                formatCategories(channelInfo.getCategories()),
                ifNullThenUsePlaceholder(channelInfo.getDescription()),
                ifNullThenUsePlaceholder(channelInfo.getGenerator()),
                ifNullThenUsePlaceholder(channelInfo.getLink()),
                ifNullThenUsePlaceholder(channelInfo.getLanguage()),
                ifNullThenUsePlaceholder(channelInfo.getUri()),
                ifNullThenUsePlaceholder(channelInfo.getPublishedDate())
        );
    }

    private final static String AUTHOR_PATTERN = "name: %s%n" +
            "email: %s%n" +
            "uri: %s";

    private String formatAuthors(List<Author> authors) {
        if (authors.isEmpty()){
            return DEFAULT_ABSENCE_SIGN;
        }else {
            return authors.stream()
                    .map(
                            author -> String.format(AUTHOR_PATTERN,
                                    ifNullThenUsePlaceholder(author.getName()),
                                    ifNullThenUsePlaceholder(author.getEmail()),
                                    ifNullThenUsePlaceholder(author.getUri())
                            )
                    ).collect(joining(lineSeparator(), lineSeparator(), ""));
        }
    }

    private String formatCategories(List<String> categories) {
        if (categories.isEmpty()) {
            return DEFAULT_ABSENCE_SIGN;
        } else {
            return String.join(",", categories);
        }
    }
}
