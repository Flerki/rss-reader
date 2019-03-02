package com.amairovi.cli.command;

import com.amairovi.core.FeedServiceFacade;
import com.amairovi.cli.CommandProcessor;

public class PollProcessor implements CommandProcessor {
    private static final String ENABLE_PATTERN = "Feed with id=%d will be polled from now on.";
    private static final String DISABLE_PATTERN = "Feed with id=%d will not be polled from now on.";

    private final FeedServiceFacade feedServiceFacade;

    public PollProcessor(FeedServiceFacade feedServiceFacade) {
        this.feedServiceFacade = feedServiceFacade;
    }

    @Override
    public void process(String[] params) {
        if (params.length != 3){
            throwUnknownCommandException(params);
        }

        String pollStatus = params[1];

        int id = Integer.valueOf(params[2]);

        if (pollStatus.equals("on")) {
            feedServiceFacade.enablePoll(id);
            String message = String.format(ENABLE_PATTERN, id);
            System.out.println(message);
            return;
        }

        if (pollStatus.equals("off")) {
            feedServiceFacade.disablePoll(id);
            String message = String.format(DISABLE_PATTERN, id);
            System.out.println(message);
            return;
        }

        throwUnknownCommandException(params);
    }

    @Override
    public String commandFormat() {
        return "poll [on | off] <feed-id>";
    }
}
