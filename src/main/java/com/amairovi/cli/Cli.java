package com.amairovi.cli;

import com.amairovi.Core;
import com.amairovi.cli.command.CreateProcessor;
import com.amairovi.cli.command.ListProcessor;

import java.util.Scanner;

public class Cli {


    private final Core core;
    private final CreateProcessor createProcessor;
    private final ListProcessor listProcessor;

    public Cli(Core core) {
        this.core = core;
        createProcessor = new CreateProcessor(core);
        listProcessor = new ListProcessor(core);
    }

    public static void main(String[] args) {
        new Cli(new Core()).start();
    }

    public void start() {

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String commandLine = scanner.nextLine().trim();
            if (commandLine.equals("stop")) {
                break;
            }

            String[] params = commandLine.split(" ");

            String command = params[0];

            String result;
            switch (command) {
                case "list":
                    listProcessor.process(params, scanner);
                    result = "success";
                    break;

                case "create":
                    createProcessor.process(params, scanner);
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
}
