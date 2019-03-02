package com.amairovi.cli.command;

import com.amairovi.cli.CommandProcessor;

import java.util.Map;

public class HelpProcessor implements CommandProcessor {
    private final Map<String, CommandProcessor> commandToProcessor;

    public HelpProcessor(Map<String, CommandProcessor> commandToProcessor) {
        this.commandToProcessor = commandToProcessor;
    }

    @Override
    public void process(String[] params) {
        if (params.length == 1) {
            commandToProcessor.values()
                    .stream()
                    .map(CommandProcessor::commandFormat)
                    .sorted()
                    .forEach(System.out::println);
            return;
        }
        if (params.length == 2) {
            String commandName = params[1];
            if (commandToProcessor.containsKey(commandName)) {
                CommandProcessor commandProcessor = commandToProcessor.get(commandName);
                System.out.println(commandProcessor.commandFormat());
            } else {
                System.out.println("Unknown command");
            }
            return;
        }

        throwUnknownCommandException(params);
    }

    @Override
    public String commandFormat() {
        return "help [<command-name>]";
    }
}
