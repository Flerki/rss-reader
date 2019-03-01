package com.amairovi.cli;

import com.amairovi.Core;
import com.amairovi.cli.command.CreateProcessor;
import com.amairovi.dto.FeedBriefInfo;

import java.util.Scanner;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;

public class Cli {


    private final Core core;
    private final CreateProcessor createProcessor;

    public Cli(Core core) {
        this.core = core;
        createProcessor = new CreateProcessor(core);
    }

    public static void main(String[] args) {
        new Cli(new Core()).start();
    }

    public void start() {

        Scanner input = new Scanner(System.in);

        while (true) {
            String commandLine = input.nextLine().trim();
            if (commandLine.equals("stop")) {
                break;
            }

            String[] params = commandLine.split(" ");

            String command = params[0];

            String result;
            switch (command) {
                case "list":
                    result = core.list()
                            .stream()
                            .map(this::convertBriefFeedInfoToString)
                            .collect(Collectors.joining(lineSeparator() + lineSeparator()));
                    break;

                case "create":
                    createProcessor.process(params);
                    result = "success";
                    break;
                case "turn-on":
                    int id = toId(params[1]);
                    core.enablePoll(id);
                    result = "success";
                    break;
                case "turn-off":
                    id = toId(params[1]);
                    core.disablePoll(id);
                    result = "success";
                    break;
                case "delete":
                    id = toId(params[1]);
                    core.delete(id);
                    result = "success";
                    break;
                case "describe":
                    id = toId(params[1]);
                    result = core.describe(id);
                    break;
                case "show":
                    String propertyName = params[1];
                    id = toId(params[2]);
                    core.showProperty(id, propertyName);
                    result = "success";
                    break;
                case "hide":
                    propertyName = params[1];
                    id = toId(params[2]);
                    core.hideProperty(id, propertyName);
                    result = "success";
                    break;
                case "translate":
                    String filename = params[1];
                    id = toId(params[2]);
                    core.redirectFeedTo(id, filename);
                    result = "success";
                    break;
                default:
                    result = "Unknown command";
            }
            System.out.println(result);
        }
    }

    private int toId(String str) {
        return Integer.valueOf(str);
    }

    private String convertBriefFeedInfoToString(FeedBriefInfo feedBriefInfo) {
        return String.format(
                "id: %d%n" +
                        "name: %s%n" +
                        "link: %s%n" +
                        "filename: %s", feedBriefInfo.getId(), feedBriefInfo.getName(), feedBriefInfo.getLink(), feedBriefInfo.getFilename());

    }
}
