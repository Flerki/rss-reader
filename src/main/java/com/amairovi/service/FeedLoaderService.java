package com.amairovi.service;

import com.amairovi.model.Feed;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class FeedLoaderService {

    public Optional<SyndFeed> load(Feed feed) {
        return retrieveUrl(feed)
                .map(this::load);
    }

    private Optional<URL> retrieveUrl(Feed feed) {
        String href = feed.getHref();
        try {
            return Optional.of(new URL(href));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private SyndFeed load(URL url) {
        try (XmlReader reader = new XmlReader(url)) {
            return new SyndFeedInput().build(reader);
        } catch (FeedException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
