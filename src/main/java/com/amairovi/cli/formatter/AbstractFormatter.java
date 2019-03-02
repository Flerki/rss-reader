package com.amairovi.cli.formatter;

abstract class AbstractFormatter {
    final static String DEFAULT_ABSENCE_SIGN = "-";

    String ifNullThenUsePlaceholder(Object object) {
        return object == null ? DEFAULT_ABSENCE_SIGN : object.toString();
    }
}
