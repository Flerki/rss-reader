package com.amairovi.cli.formatter;

import com.amairovi.core.dto.ChannelInfo;
import com.amairovi.core.dto.FeedInfo;

import java.util.Map;

import static java.lang.System.lineSeparator;

public class FeedConfigsFormatter extends AbstractFormatter {

    private final static String PATTERN =
            "Feed configs%n" +
                    "id: %s%n" +
                    "name: %s%n" +
                    "link: %s%n" +
                    "filename: %s%n" +
                    "amount of elements polled at once: %s%n" +
                    "poll period in ms: %s%n" +
                    "polling is enabled: %b%n";

    private final ChannelInfoFormatter channelInfoFormatter;
    private final EntryPropertiesFormatter entryPropertiesFormatter;

    public FeedConfigsFormatter(ChannelInfoFormatter channelInfoFormatter,
                                EntryPropertiesFormatter entryPropertiesFormatter) {
        this.channelInfoFormatter = channelInfoFormatter;
        this.entryPropertiesFormatter = entryPropertiesFormatter;
    }

    public String formatDeep(FeedInfo feedInfo){
        StringBuilder str = new StringBuilder();
        ChannelInfo channelInfo = feedInfo.getChannelInfo();
        Map<String, Boolean> entryParameterNameToVisibility = feedInfo.getEntryParameterNameToVisibility();
        String entryProperties = entryPropertiesFormatter.format(entryParameterNameToVisibility);

        return str.append(format(feedInfo))
                .append(lineSeparator())
                .append(channelInfoFormatter.format(channelInfo))
                .append(lineSeparator())
                .append(entryProperties)
                .append(lineSeparator())
                .toString();
    }

    public String format(FeedInfo feedInfo) {
        return String.format(PATTERN,
                ifNullThenUsePlaceholder(feedInfo.getId()),
                ifNullThenUsePlaceholder(feedInfo.getName()),
                ifNullThenUsePlaceholder(feedInfo.getLink()),
                ifNullThenUsePlaceholder(feedInfo.getFilename()),
                ifNullThenUsePlaceholder(feedInfo.getAmountOfElementsAtOnce()),
                ifNullThenUsePlaceholder(feedInfo.getPollPeriodInMs()),
                feedInfo.isPolled()

        );
    }
}
