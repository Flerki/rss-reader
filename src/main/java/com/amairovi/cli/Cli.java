package com.amairovi.cli;

import com.amairovi.core.FeedServiceFacade;
import com.amairovi.cli.command.*;
import com.amairovi.cli.formatter.FeedConfigsFormatter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Cli {
    private final Scanner scanner;

    private final Map<String, CommandProcessor> commandToProcessor;

    private final UnknownCommandProcessor unknownCommandProcessor;

    public Cli(FeedServiceFacade feedServiceFacade) {
        scanner = new Scanner(System.in);
        commandToProcessor = new HashMap<>();
        unknownCommandProcessor = new UnknownCommandProcessor();

        FeedConfigsFormatter feedConfigsFormatter = new FeedConfigsFormatter();

        commandToProcessor.put("list", new ListProcessor(feedServiceFacade));
        commandToProcessor.put("create", new CreateProcessor(feedServiceFacade, feedConfigsFormatter, scanner));
        commandToProcessor.put("stop", new StopProcessor(feedServiceFacade));
        commandToProcessor.put("show", new ShowPropertyProcessor(feedServiceFacade));
        commandToProcessor.put("hide", new HidePropertyProcessor(feedServiceFacade));
        commandToProcessor.put("poll", new PollProcessor(feedServiceFacade));
        commandToProcessor.put("set", new SetProcessor(feedServiceFacade));
        commandToProcessor.put("delete", new DeleteProcessor(feedServiceFacade));
    }

    public void start() {
        while (true) {
            String commandLine = scanner.nextLine().trim();
            try {
                String[] params = commandLine.split(" ");

                String command = params[0];

                CommandProcessor processor = commandToProcessor.getOrDefault(command, unknownCommandProcessor);
                processor.process(params);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
