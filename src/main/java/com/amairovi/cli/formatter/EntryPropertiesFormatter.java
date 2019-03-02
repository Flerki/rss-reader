package com.amairovi.cli.formatter;

import java.util.Map;

import static java.lang.System.lineSeparator;

public class EntryPropertiesFormatter extends AbstractFormatter{
    private final static String PATTERN = "%s: %s%n";

    public String format(Map<String, Boolean> entryParameterNameToVisibility) {
        if (entryParameterNameToVisibility.isEmpty()){
            return "There is no property.";
        }
        StringBuilder str = new StringBuilder();
        str.append("Feed's entries' properties")
                .append(lineSeparator());

        entryParameterNameToVisibility.keySet()
                .stream()
                .sorted()
                .map(propertyName -> String.format(PATTERN, propertyName, entryParameterNameToVisibility.get(propertyName) ? "show" : "hide"))
                .forEach(str::append);
        return str.toString();
    }
}
