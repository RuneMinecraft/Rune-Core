package com.dank1234.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class DateUtils {
    public static String fromLong(long length) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(length);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(length);
        long hours = TimeUnit.MILLISECONDS.toHours(length);
        long days = TimeUnit.MILLISECONDS.toDays(length);
        long weeks = days / 7;
        long months = days / 30;
        long years = days / 365;

        if (seconds < 60) {
            return format(seconds, "second");
        } else if (minutes < 60) {
            return format(minutes, "minute");
        } else if (hours < 24) {
            return format(hours, "hour");
        } else if (days < 7) {
            return format(days, "day");
        } else if (weeks < 4) {
            return format(weeks, "week");
        } else if (months < 12) {
            return format(months, "month");
        } else {
            return format(years, "year");
        }
    }

    private static String format(long value, String unit) {
        if (value == 1) {
            return "for 1 " + unit+" ";
        } else {
            return "for " + value + " " + unit + "s ";
        }
    }

    public static String date(long time) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(time));
    }
}
