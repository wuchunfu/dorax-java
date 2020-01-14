package org.dorax.utils;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Helper for output KSQL welcome messages to the console.
 *
 * @author wuchunfu
 * @date 2020-01-14
 */
public class WelcomeMsgUtils {

    private WelcomeMsgUtils() {
    }

    /**
     * Output a welcome message to the console
     */
    public static void displayWelcomeMessage(final int consoleWidth, final PrintWriter writer) {
        final String[] lines = {
                "",
                "===========================================",
                "=       _              _ ____  ____       =",
                "=      | | _____  __ _| |  _ \\| __ )      =",
                "=      | |/ / __|/ _` | | | | |  _ \\      =",
                "=      |   <\\__ \\ (_| | | |_| | |_) |     =",
                "=      |_|\\_\\___/\\__, |_|____/|____/      =",
                "=                   |_|                   =",
                "=  Event Streaming Database purpose-built =",
                "=        for stream processing apps       =",
                "==========================================="
        };
        final String copyrightMsg = "Copyright 2017-2019 Confluent Inc.";
        final Integer logoWidth = Arrays.stream(lines).map(String::length).reduce(0, Math::max);
        // Don't want to display the logo if it'll just end up getting wrapped and looking hideous
        if (consoleWidth < logoWidth) {
            writer.println("ksqlDB, " + copyrightMsg);
        } else {
            final int paddingChars = (consoleWidth - logoWidth) / 2;
            final String leftPadding = IntStream.range(0, paddingChars).mapToObj(idx -> " ").collect(Collectors.joining());
            Arrays.stream(lines).forEach(line -> writer.println(leftPadding + line));
            writer.println();
            writer.println(copyrightMsg);
        }
        writer.println();
        writer.flush();
    }
}
