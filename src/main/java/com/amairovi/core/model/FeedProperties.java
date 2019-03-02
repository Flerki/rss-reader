package com.amairovi.core.model;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndFeed;

import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FeedProperties {

    private List<FeedAuthor> authors;
    private List<String> categories;
    private String title;
    private String description;
    private String generator;
    private String link;
    private String language;
    private String uri;
    private Date publishedDate;

    public FeedProperties() {
    }

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

    public void setAuthors(List<FeedAuthor> authors) {
        this.authors = authors;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }
}
