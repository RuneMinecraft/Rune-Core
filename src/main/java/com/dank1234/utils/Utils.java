package com.dank1234.utils;

import com.dank1234.plugin.Main;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

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
                            // Double the color code
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

    default void sendMessage(CommandSender player, String message) {
        player.sendMessage(Colour(message));
    }
    default void permissionError(CommandSender player) {
        sendMessage(player, "&cNo Permission!");
    }
    default void invalidArgumentsError(CommandSender player) {
        sendMessage(player, "&cInvalid Arguments!");
    }
    default void nullPlayerError(CommandSender player) {
        sendMessage(player, "&cThat player is not online or does not exist!");
    }
    default void nullPlayerError(CommandSender player, String name) {
        sendMessage(player, "&cThe player &e&o" + name + " &cis not online or does not exist!");
    }
    default void customError(CommandSender player, String error) {
        sendMessage(player, "&c" + error);
    }
    default void ExceptionThrownMessage(CommandSender player) {
        sendMessage(player, "&cAn unexpected error has been thrown.\nPlease make a ticket in the discord and wait for an &4Admin+&c.");
    }

    default String getSimpleName(ItemStack stack) {
        String name = stack.getType().name();
        StringBuilder formattedName = new StringBuilder();
        String[] words = name.split("_");
        for (String word : words) {
            formattedName.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ");
        }
        return formattedName.toString();
    }
    default Object getKey(Object value, Map<?, ?> map) {
        for (Object key : map.keySet()) {
            if (map.get(key).equals(value)) return key;
        }
        return null;
    }
}
