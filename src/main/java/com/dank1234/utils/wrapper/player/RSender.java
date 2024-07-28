package com.dank1234.utils.wrapper.player;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class RSender {
    private final CommandSender player;

    RSender(final CommandSender sender) {
        this.player = sender;
    }
    public static RSender of(final CommandSender sender) {
        return new RSender(sender);
    }
    public static RSender of(final String username) {
        return RSender.of((username.equals("console") ? Bukkit.getConsoleSender() : Bukkit.getPlayer(username)));
    }
    public static RSender of(final UUID id) {
        return RSender.of(Bukkit.getPlayer(id));
    }

    public CommandSender get() {
        return this.player;
    }
}
