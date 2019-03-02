package com.amairovi;

import com.amairovi.cli.Cli;
import com.amairovi.core.FeedServiceFacade;

import java.io.IOException;
import java.util.logging.*;

public class App {
    public static void main(String[] args) throws IOException {
        setupLogging();

        FeedServiceFacade feedServiceFacade = new FeedServiceFacade();
        feedServiceFacade.initialize();

        Cli cli = new Cli(feedServiceFacade);
        cli.start();
    }

    private static void setupLogging() throws IOException {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.ALL);

        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        FileHandler fileTxt = new FileHandler("logs.txt", true);

        SimpleFormatter formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        rootLogger.addHandler(fileTxt);
    }
}
