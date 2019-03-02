package com.amairovi.cli.command;

import com.amairovi.core.FeedServiceFacade;
import com.amairovi.cli.CommandProcessor;

public class SetProcessor implements CommandProcessor {

    private final FeedServiceFacade feedServiceFacade;

    public SetProcessor(FeedServiceFacade feedServiceFacade) {
        this.feedServiceFacade = feedServiceFacade;
    }

    @Override
    public void process(String[] params) {
        if (params.length != 4) {
            throwUnknownCommandException(params);
        }

        String property = params[1];

        if (property.equals("poll-period")) {
            long pollPeriodInSec = Integer.valueOf(params[2]);
            int id = Integer.valueOf(params[3]);
            feedServiceFacade.setPollPeriodInMs(id, pollPeriodInSec * 1000);
            return;
        }

        if (property.equals("filename")) {
            String filename = params[2];
            int id = Integer.valueOf(params[3]);
            feedServiceFacade.setFeedFilename(id, filename);
            return;
        }

        if (property.equals("item-amount")) {
            int itemAmount = Integer.valueOf(params[2]);
            int id = Integer.valueOf(params[3]);
            feedServiceFacade.setFeedAmountOfElementsAtOnce(id, itemAmount);
            return;
        }

        if(property.equals("name")){
            String name = params[2];
            int id = Integer.valueOf(params[3]);
            feedServiceFacade.setFeedName(id, name);
            return;
        }

        throwUnknownCommandException(params);
    }

    @Override
    public String commandFormat() {
        return "set [poll-period | filename | item-amount | name] <poll-period-in-sec | filename | item-amount | name> <feed-id>";
    }
}

