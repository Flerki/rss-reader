package com.amairovi.core.service;

import com.amairovi.core.model.Feed;

import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class EntryPropertiesService {

    private final static String ENTRY_NOT_FOUND_PATTERN = "Entry's parameter with name=%s for feed with id=%d is not presented";

    public void addParameter(Feed feed, String parameterName) {
        Map<String, Boolean> entryParameterNameToVisibility = feed.getEntryParameterNameToVisibility();
        if (!entryParameterNameToVisibility.containsKey(parameterName)) {
            entryParameterNameToVisibility.put(parameterName, TRUE);
        }
    }

    public void hideParameter(Feed feed, String parameterName) {
        setParameterVisibility(feed, parameterName, FALSE);
    }

    public void showParameter(Feed feed, String parameterName) {
        setParameterVisibility(feed, parameterName, TRUE);
    }

    private void setParameterVisibility(Feed feed, String parameterName, boolean isVisible) {
        Map<String, Boolean> entryParameterNameToVisibility = feed.getEntryParameterNameToVisibility();
        if (!entryParameterNameToVisibility.containsKey(parameterName)) {
            String message = String.format(ENTRY_NOT_FOUND_PATTERN, parameterName, feed.getId());
            throw new IllegalArgumentException(message);
        }
        entryParameterNameToVisibility.put(parameterName, isVisible);
    }
}
