package com.amairovi.core.model;

import com.rometools.rome.feed.synd.SyndPerson;

public class FeedAuthor {
    private String email;
    private String name;
    private String uri;

    public FeedAuthor() {
    }

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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
