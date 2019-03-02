package com.amairovi;

import com.amairovi.cli.Cli;
import com.amairovi.core.FeedServiceFacade;

import java.util.logging.LogManager;

public class App {
    public static void main(String[] args) {
        LogManager logManager = LogManager.getLogManager();
        logManager.reset();
        FeedServiceFacade feedServiceFacade = new FeedServiceFacade();
        new Cli(feedServiceFacade).start();
    }
}
