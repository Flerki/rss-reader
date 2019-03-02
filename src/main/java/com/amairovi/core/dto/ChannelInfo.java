package com.amairovi.core.dto;

import com.amairovi.core.model.FeedProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ChannelInfo {
    private List<Author> authors;
    private List<String> categories;
    private String title;
    private String description;
    private String generator;
    private String link;
    private String language;
    private String uri;
    private Date publishedDate;

    public ChannelInfo(FeedProperties properties) {
        authors = properties.getAuthors()
                .stream()
                .map(Author::new)
                .collect(toList());
        categories = new ArrayList<>(properties.getCategories());

        title = properties.getTitle();
        description = properties.getDescription();
        generator = properties.getGenerator();
        link = properties.getLink();
        language = properties.getLanguage();
        uri = properties.getUri();
        publishedDate = properties.getPublishedDate();
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenerator() {
        return generator;
    }

    public String getLink() {
        return link;
    }

    public String getLanguage() {
        return language;
    }

    public String getUri() {
        return uri;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }
}
