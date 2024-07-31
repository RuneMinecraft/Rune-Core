package com.dank1234.utils;

import java.util.Arrays;

public final class Logger {
    public static void log(String ... messages) {
        Arrays.stream(messages).toList().forEach(message -> {
            System.out.println("[Rune] [LOG] " + message);
        });
    }
    public static void logRaw(String ... messages) {
        Arrays.stream(messages).toList().forEach(System.out::println);
    }
}
