package com.amairovi.service;

import com.amairovi.model.Feed;
import com.amairovi.model.FeedFile;
import com.amairovi.model.FeedProperties;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.jdom2.Element;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

public class LoadTaskFactory {
    private static final Logger LOGGER = Logger.getLogger(LoadTaskFactory.class.getName());

    private final FeedFileService fileService;
    private final FeedLoaderService feedLoaderService;
    private final FeedFormatter feedFormatter;

    public LoadTaskFactory(FeedFileService fileService, FeedLoaderService feedLoaderService, FeedFormatter feedFormatter) {
        this.fileService = fileService;
        this.feedLoaderService = feedLoaderService;
        this.feedFormatter = feedFormatter;
    }

    public Runnable create(Feed feed) {
        LOGGER.log(Level.INFO, "create()");
        requireNonNull(feed);

        FeedFile feedFile = new FeedFile();
        feedFile.setFilename(feed.getName());

        return () -> {
            LOGGER.log(Level.INFO, () -> "run load task for feed=" + feed);

            Optional<SyndFeed> syndFeedOptional = feedLoaderService.load(feed);
            if (syndFeedOptional.isPresent()) {
                SyndFeed syndFeed = syndFeedOptional.get();

                updateFeed(syndFeed, feed);

                String syndFeedStr = feedFormatter.format(syndFeed, feed);;
                fileService.writeln(feedFile, syndFeedStr);
            }
        };
    }

    private void updateFeed(SyndFeed syndFeed, Feed feed) {
        FeedProperties feedProperties = new FeedProperties(syndFeed);
        feed.setFeedProperties(feedProperties);

        for (SyndEntry entry : syndFeed.getEntries()) {
            checkEntryParameterPresenseAndAddIfNecessary("title", entry.getTitle(), feed);
            checkEntryParameterPresenseAndAddIfNecessary("author", entry.getAuthor(), feed);
            checkEntryParameterPresenseAndAddIfNecessary("comments", entry.getComments(), feed);
            checkEntryParameterPresenseAndAddIfNecessary("link", entry.getLink(), feed);
            checkEntryParameterPresenseAndAddIfNecessary("description", entry.getDescription(), feed);
            checkEntryParameterPresenseAndAddIfNecessary("publishedDate", entry.getPublishedDate(), feed);
            checkEntryParameterPresenseAndAddIfNecessary("updatedDate", entry.getUpdatedDate(), feed);
            checkEntryParameterPresenseAndAddIfNecessary("uri", entry.getUri(), feed);

            if (!entry.getContents().isEmpty()){
                feed.addParameter("content");
            }

            if (!entry.getCategories().isEmpty()){
                feed.addParameter("categories");
            }

            entry.getForeignMarkup()
                    .stream()
                    .map(Element::getName)
                    .forEach(feed::addParameter);

        }
    }

    private void checkEntryParameterPresenseAndAddIfNecessary(String name, Object param, Feed feed) {
        if (param != null) {
            feed.addParameter(name);
        }
    }
}
