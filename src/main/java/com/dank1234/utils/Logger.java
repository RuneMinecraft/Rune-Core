package com.dank1234.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public final class Logger {
    static ConsoleCommandSender console = Bukkit.getConsoleSender();

    public static void log(String ... s) {
        List.of(s).forEach((message) -> console.sendMessage(message));
    }
    public static void info(String ... s) {
        List.of(s).forEach((message) -> console.sendMessage(Color.WHITE+"[INFO] [Rune-Core] "+message+Color.RESET));
    }
    public static void infoRaw(String ... s) {
        List.of(s).forEach((message) -> console.sendMessage(Color.WHITE+"[INFO] "+message+Color.RESET));
    }
    public static void warn(String ... s) {
        List.of(s).forEach((message) -> console.sendMessage(Color.YELLOW+"[WARN] [Rune-Core] "+message+Color.RESET));
    }
    public static void warnRaw(String ... s) {
        List.of(s).forEach((message) -> console.sendMessage(Color.YELLOW+"[WARN] "+message+Color.RESET));
    }
    public static void error(String ... s) {
        List.of(s).forEach((message) -> console.sendMessage(Color.RED+"[ERROR] [Rune-Core] "+message+Color.RESET));
    }
    public static void errorRaw(String ... s) {
        List.of(s).forEach((message) -> console.sendMessage(Color.RED+"[ERROR] "+message+Color.RESET));
    }

    public static class Color {
        public static final String RESET = "\u001B[0m";
        public static final String WHITE = "\u001B[37m";
        public static final String YELLOW = "\u001B[33m";
        public static final String RED = "\u001B[31m";
    }

    private static String getTime() {
        var formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }
}
