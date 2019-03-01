package com.amairovi.service;

import com.amairovi.model.Feed;
import com.amairovi.model.FeedProperties;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.jdom2.Element;

public class FeedStateService {
    private final EntryPropertiesService entryPropertiesService;

    public FeedStateService(EntryPropertiesService entryPropertiesService) {
        this.entryPropertiesService = entryPropertiesService;
    }

    public void update(SyndFeed syndFeed, Feed feed) {
        FeedProperties feedProperties = new FeedProperties(syndFeed);

        feed.setFeedProperties(feedProperties);

        for (SyndEntry entry : syndFeed.getEntries()) {
            checkEntryParameterPresenceAndAddIfNecessary("title", entry.getTitle(), feed);
            checkEntryParameterPresenceAndAddIfNecessary("author", entry.getAuthor(), feed);
            checkEntryParameterPresenceAndAddIfNecessary("comments", entry.getComments(), feed);
            checkEntryParameterPresenceAndAddIfNecessary("link", entry.getLink(), feed);
            checkEntryParameterPresenceAndAddIfNecessary("description", entry.getDescription(), feed);
            checkEntryParameterPresenceAndAddIfNecessary("publishedDate", entry.getPublishedDate(), feed);
            checkEntryParameterPresenceAndAddIfNecessary("updatedDate", entry.getUpdatedDate(), feed);
            checkEntryParameterPresenceAndAddIfNecessary("uri", entry.getUri(), feed);

            if (!entry.getContents().isEmpty()) {
                entryPropertiesService.addParameter(feed, "content");
            }

            if (!entry.getCategories().isEmpty()) {
                entryPropertiesService.addParameter(feed, "categories");
            }

            entry.getForeignMarkup()
                    .stream()
                    .map(Element::getName)
                    .forEach(parameterName -> entryPropertiesService.addParameter(feed, parameterName));
        }
    }

    private void checkEntryParameterPresenceAndAddIfNecessary(String name, Object param, Feed feed) {
        if (param != null) {
            entryPropertiesService.addParameter(feed, name);
        }
    }
}
