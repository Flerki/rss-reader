package com.amairovi.cli.command;

import com.amairovi.cli.CommandProcessor;
import com.amairovi.core.FeedServiceFacade;

public class DeleteProcessor implements CommandProcessor {
    private static final String PATTERN = "Feed with id=%d is deleted.";

    private final FeedServiceFacade feedServiceFacade;

    public DeleteProcessor(FeedServiceFacade feedServiceFacade) {
        this.feedServiceFacade = feedServiceFacade;
    }

    @Override
    public void process(String[] params) {
        if (params.length != 2){
            throwUnknownCommandException(params);
        }

        int id = Integer.valueOf(params[1]);
        feedServiceFacade.delete(id);

        String successMessage = String.format(PATTERN, id);
        System.out.println(successMessage);
    }

    @Override
    public String commandFormat() {
        return "delete <feedId>";
    }
}
