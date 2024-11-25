package com.dank1234.utils;

import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.ChatColor;

public interface Utils {
    default String Colour(String s) {
        final char altColorChar = '&';
        final StringBuilder b = new StringBuilder();
        final char[] mess = s.toCharArray();
        boolean color = false, hashtag = false, doubleTag = false;
        char tmp;
        for (int i = 0; i < mess.length; ) {
            final char c = mess[i];
            if (doubleTag) {
                doubleTag = false;
                final int max = i + 3;
                if (max <= mess.length) {
                    boolean match = true;
                    for (int n = i; n < max; n++) {
                        tmp = mess[n];
                        if (!((tmp >= '0' && tmp <= '9') || (tmp >= 'a' && tmp <= 'f') || (tmp >= 'A' && tmp <= 'F'))) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        b.append(ChatColor.COLOR_CHAR);
                        b.append('x');
                        for (; i < max; i++) {
                            tmp = mess[i];
                            b.append(ChatColor.COLOR_CHAR);
                            b.append(tmp);
                            b.append(ChatColor.COLOR_CHAR);
                            b.append(tmp);
                        }
                        continue;
                    }
                }
                b.append(altColorChar);
                b.append("##");
            }
            if (hashtag) {
                hashtag = false;
                if (c == '#') {
                    doubleTag = true;
                    i++;
                    continue;
                }
                final int max = i + 6;
                if (max <= mess.length) {
                    boolean match = true;
                    for (int n = i; n < max; n++) {
                        tmp = mess[n];
                        if (!((tmp >= '0' && tmp <= '9') || (tmp >= 'a' && tmp <= 'f') || (tmp >= 'A' && tmp <= 'F'))) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        b.append(ChatColor.COLOR_CHAR);
                        b.append('x');
                        for (; i < max; i++) {
                            b.append(ChatColor.COLOR_CHAR);
                            b.append(mess[i]);
                        }
                        continue;
                    }
                }
                b.append(altColorChar);
                b.append('#');
            }
            if (color) {
                color = false;
                if (c == '#') {
                    hashtag = true;
                    i++;
                    continue;
                }
                if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || c == 'r' || (c >= 'k' && c <= 'o') || (c >= 'A' && c <= 'F') || c == 'R' || (c >= 'K' && c <= 'O')) {
                    b.append(ChatColor.COLOR_CHAR);
                    b.append(c);
                    i++;
                    continue;
                }
                b.append(altColorChar);
            }
            if (c == altColorChar) {
                color = true;
                i++;
                continue;
            }
            b.append(c);
            i++;
        }
        if (color) b.append(altColorChar);
        else if (hashtag) {
            b.append(altColorChar);
            b.append('#');
        } else if (doubleTag) {
            b.append(altColorChar);
            b.append("##");
        }
        return b.toString();
    }
    static String sColour(String s) {
        final char altColorChar = '&';
        final StringBuilder b = new StringBuilder();
        final char[] mess = s.toCharArray();
        boolean color = false, hashtag = false, doubleTag = false;
        char tmp;
        for (int i = 0; i < mess.length; ) {
            final char c = mess[i];
            if (doubleTag) {
                doubleTag = false;
                final int max = i + 3;
                if (max <= mess.length) {
                    boolean match = true;
                    for (int n = i; n < max; n++) {
                        tmp = mess[n];
                        if (!((tmp >= '0' && tmp <= '9') || (tmp >= 'a' && tmp <= 'f') || (tmp >= 'A' && tmp <= 'F'))) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        b.append(ChatColor.COLOR_CHAR);
                        b.append('x');
                        for (; i < max; i++) {
                            tmp = mess[i];
                            b.append(ChatColor.COLOR_CHAR);
                            b.append(tmp);
                            b.append(ChatColor.COLOR_CHAR);
                            b.append(tmp);
                        }
                        continue;
                    }
                }
                b.append(altColorChar);
                b.append("##");
            }
            if (hashtag) {
                hashtag = false;
                if (c == '#') {
                    doubleTag = true;
                    i++;
                    continue;
                }
                final int max = i + 6;
                if (max <= mess.length) {
                    boolean match = true;
                    for (int n = i; n < max; n++) {
                        tmp = mess[n];
                        if (!((tmp >= '0' && tmp <= '9') || (tmp >= 'a' && tmp <= 'f') || (tmp >= 'A' && tmp <= 'F'))) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        b.append(ChatColor.COLOR_CHAR);
                        b.append('x');
                        for (; i < max; i++) {
                            b.append(ChatColor.COLOR_CHAR);
                            b.append(mess[i]);
                        }
                        continue;
                    }
                }
                b.append(altColorChar);
                b.append('#');
            }
            if (color) {
                color = false;
                if (c == '#') {
                    hashtag = true;
                    i++;
                    continue;
                }
                if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || c == 'r' || (c >= 'k' && c <= 'o') || (c >= 'A' && c <= 'F') || c == 'R' || (c >= 'K' && c <= 'O')) {
                    b.append(ChatColor.COLOR_CHAR);
                    b.append(c);
                    i++;
                    continue;
                }
                b.append(altColorChar);
            }
            if (c == altColorChar) {
                color = true;
                i++;
                continue;
            }
            b.append(c);
            i++;
        }
        if (color) b.append(altColorChar);
        else if (hashtag) {
            b.append(altColorChar);
            b.append('#');
        } else if (doubleTag) {
            b.append(altColorChar);
            b.append("##");
        }
        return b.toString();
    }
    default String centreText(String text) {
        int padding = (55 - text.length()) / 2;
        StringBuilder paddedText = new StringBuilder();

        paddedText.append("-".repeat(Math.max(0, padding)));

        paddedText.append(text);

        while (paddedText.length() < 55) {
            paddedText.append("-");
        }

        return paddedText.toString();
    }

    default boolean checkExpression(boolean expression, Message msg) {
        if (!expression) msg.send();
        return expression;
    }
    default void checkExpression(boolean expression, String msg) {
        try {
            if (!expression) {
                throw new Exception(msg);
            }
        } catch(Exception ignored){}
    }
}
