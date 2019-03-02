package com.amairovi.cli.command;

import com.amairovi.cli.CommandProcessor;
import com.amairovi.cli.formatter.FeedConfigsFormatter;
import com.amairovi.core.FeedServiceFacade;
import com.amairovi.core.dto.FeedInfo;

public class DescribeProcessor implements CommandProcessor {
    private final FeedServiceFacade feedServiceFacade;
    private final FeedConfigsFormatter feedConfigsFormatter;

    public DescribeProcessor(FeedServiceFacade feedServiceFacade,
                             FeedConfigsFormatter feedConfigsFormatter) {
        this.feedServiceFacade = feedServiceFacade;
        this.feedConfigsFormatter = feedConfigsFormatter;
    }

    @Override
    public void process(String[] params) {
        if (params.length != 2){
            throwUnknownCommandException(params);
        }

        int feedId = Integer.valueOf(params[1]);
        printFeedInfo(feedId);
    }

    private void printFeedInfo(int feedId) {
        FeedInfo feedFullDescription = feedServiceFacade.getFeedFullDescription(feedId);
        System.out.println(feedConfigsFormatter.formatDeep(feedFullDescription));
    }


    @Override
    public String commandFormat() {
        return "describe <feed-id>";
    }
}
