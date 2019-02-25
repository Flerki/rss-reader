package com.amairovi.model;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndPerson;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FeedProperties {

    private final List<FeedAuthor> authors;
    private final List<String> categories;
    private final String title;
    private final String description;
    private final String generator;
    private final String link;
    private final String language;
    private final String uri;
    private final Date publishedDate;

    public FeedProperties(SyndFeed syndFeed) {
        authors = syndFeed.getAuthors()
                .stream()
                .map(FeedAuthor::new)
                .collect(toList());
        categories = syndFeed.getCategories()
                .stream()
                .map(SyndCategory::getName)
                .collect(toList());

        title = syndFeed.getTitle();
        description = syndFeed.getDescription();
        generator = syndFeed.getGenerator();
        link = syndFeed.getLink();
        language = syndFeed.getLanguage();
        uri = syndFeed.getUri();
        publishedDate = syndFeed.getPublishedDate();

    }

    public List<FeedAuthor> getAuthors() {
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

    private static class FeedAuthor {
        private final String email;
        private final String name;
        private final String uri;

        public FeedAuthor(SyndPerson syndPerson) {
            email = syndPerson.getEmail();
            name = syndPerson.getName();
            uri = syndPerson.getUri();
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

        public String getUri() {
            return uri;
        }
    }
}
