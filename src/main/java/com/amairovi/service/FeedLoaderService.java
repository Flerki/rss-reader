package com.amairovi.service;

import com.amairovi.exception.FeedProcessingException;
import com.amairovi.exception.InvalidLinkFormatException;
import com.amairovi.model.Feed;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FeedLoaderService {
    private static final Logger LOGGER = Logger.getLogger(FeedLoaderService.class.getName());

    public SyndFeed load(Feed feed) {
        LOGGER.log(Level.INFO, () -> "load for feed=" + feed);
        URL url = retrieveUrl(feed);
        return load(url);
    }

    private URL retrieveUrl(Feed feed) {
        String href = feed.getHref();
        try {
            return new URL(href);
        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new InvalidLinkFormatException(e);
        }
    }

    private SyndFeed load(URL url) {
        try (XmlReader reader = new XmlReader(url)) {
            return new SyndFeedInput().build(reader);
        } catch (FeedException | IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new FeedProcessingException(e);
        }
    }

}
