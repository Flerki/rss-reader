package com.amairovi.core.service.polling;

import com.amairovi.core.exception.FeedProcessingException;
import com.amairovi.core.exception.InvalidLinkFormatException;
import com.amairovi.core.model.Feed;
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
        return load(feed.getHref());
    }

    public SyndFeed load(String urlStr) {
        LOGGER.log(Level.INFO, () -> "load by url=" + urlStr);
        URL url = toURL(urlStr);
        return load(url);
    }

    private URL toURL(String urlStr) {
        try {
            return new URL(urlStr);
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
