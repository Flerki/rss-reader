package com.amairovi.cli;

import com.amairovi.Core;
import com.amairovi.cli.command.CreateProcessor;
import com.amairovi.cli.command.ListProcessor;
import com.amairovi.cli.command.StopProcessor;
import com.amairovi.cli.command.UnknownCommandProcessor;
import com.amairovi.cli.formatter.FeedConfigsFormatter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Cli {
    private final Scanner scanner;

    private final Map<String, CommandProcessor> commandToProcessor;

    private final UnknownCommandProcessor unknownCommandProcessor;

    public static void main(String[] args) {
        Core core = new Core();
        new Cli(core).start();
    }

    public Cli(Core core) {
        scanner = new Scanner(System.in);
        commandToProcessor = new HashMap<>();
        unknownCommandProcessor = new UnknownCommandProcessor();

        FeedConfigsFormatter feedConfigsFormatter = new FeedConfigsFormatter();

        commandToProcessor.put("list", new ListProcessor(core));
        commandToProcessor.put("create", new CreateProcessor(core, feedConfigsFormatter, scanner));
        commandToProcessor.put("stop", new StopProcessor(core));
    }

    public void start() {

        while (true) {
            String commandLine = scanner.nextLine().trim();
            String[] params = commandLine.split(" ");

            String command = params[0];

            CommandProcessor processor = commandToProcessor.getOrDefault(command, unknownCommandProcessor);
            processor.process(params);
//            String result;
//
//
//            switch (command) {
//                case "turn-on":
//                    int id = toId(params[1]);
//                    core.enablePoll(id);
//                    result = "success";
//                    break;
//                case "turn-off":
//                    id = toId(params[1]);
//                    core.disablePoll(id);
//                    result = "success";
//                    break;
//                case "delete":
//                    id = toId(params[1]);
//                    core.delete(id);
//                    result = "success";
//                    break;
//                case "describe":
//                    id = toId(params[1]);
//                    result = core.describe(id);
//                    break;
//                case "show":
//                    String propertyName = params[1];
//                    id = toId(params[2]);
//                    core.showProperty(id, propertyName);
//                    result = "success";
//                    break;
//                case "hide":
//                    propertyName = params[1];
//                    id = toId(params[2]);
//                    core.hideProperty(id, propertyName);
//                    result = "success";
//                    break;
//                case "translate":
//                    String filename = params[1];
//                    id = toId(params[2]);
//                    core.redirectFeedTo(id, filename);
//                    result = "success";
//                    break;
//            }
        }
    }

    private int toId(String str) {
        return Integer.valueOf(str);
    }
}
