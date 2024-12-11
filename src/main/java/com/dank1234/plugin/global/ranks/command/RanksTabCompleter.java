package com.dank1234.plugin.global.ranks.command;

import com.dank1234.plugin.Main;
import com.dank1234.plugin.global.ranks.Group;
import com.dank1234.plugin.global.ranks.Track;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RanksTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions = List.of("user", "group", "track", "promote", "demote");
        } else if (args.length >= 2) {
            switch (args[0].toLowerCase()) {
                case "user" -> {
                    if (args.length == 2) {
                        //completions = getPlayers();
                    } else if (args.length == 3) {
                        completions = List.of("group", "info", "track", "permissions");
                    } else if (args[2].equalsIgnoreCase("group") && args.length == 4) {
                        completions = List.of("set", "add", "remove");
                    } else if (args[2].equalsIgnoreCase("track") && args.length == 4) {
                        completions = List.of("set", "remove");
                    } else if (args[2].equalsIgnoreCase("permissions") && args.length == 4) {
                        completions = List.of("add", "remove");
                    }
                }
                case "group" -> {
                    if (args.length == 2) {
                        //completions = getGroups();
                    } else if (args.length == 3) {
                        completions = List.of("info", "name", "prefix", "suffix", "inherited-groups", "permissions");
                    } else if (args[2].equalsIgnoreCase("name") && args.length == 4) {
                        completions = List.of("set");
                    } else if (args[2].equalsIgnoreCase("prefix") && args.length == 4) {
                        completions = List.of("set");
                    } else if (args[2].equalsIgnoreCase("suffix") && args.length == 4) {
                        completions = List.of("set");
                    } else if (args[2].equalsIgnoreCase("inherited-groups") && args.length == 4) {
                        completions = List.of("add", "remove");
                    } else if (args[2].equalsIgnoreCase("permissions") && args.length == 4) {
                        completions = List.of("add", "remove");
                    }
                }
                case "track" -> {
                    if (args.length == 2) {
                        //completions = getTracks();
                    } else if (args.length == 3) {
                        completions = List.of("info", "group");
                    } else if (args[2].equalsIgnoreCase("group") && args.length == 4) {
                        completions = List.of("add", "remove");
                    }
                }
                case "promote", "demote" -> {
                    if (args.length == 2) {
                        //completions = getPlayers(); // Suggest player names
                    } else if (args.length == 3) {
                        //completions = getTracks(); // Suggest track names
                    }
                }
            }
        }

        return filterByInput(args[args.length - 1], completions);
    }

    /**
     * Filters the provided completions based on the user's input.
     *
     * @param input       The user's current input.
     * @param completions The list of possible completions.
     * @return A filtered list of completions.
     */
    private List<String> filterByInput(String input, List<String> completions) {
        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(input.toLowerCase()))
                .collect(Collectors.toList());
    }
}
