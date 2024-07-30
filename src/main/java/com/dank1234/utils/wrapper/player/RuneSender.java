package com.dank1234.utils.wrapper.player;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class RuneSender {
    private final CommandSender player;

    RuneSender(final CommandSender sender) {
        this.player = sender;
    }
    public static RuneSender of(final CommandSender sender) {
        return new RuneSender(sender);
    }
    public static RuneSender of(final String username) {
        return RuneSender.of((username.equals("console") ? Bukkit.getConsoleSender() : Bukkit.getPlayer(username)));
    }
    public static RuneSender of(final UUID id) {
        return RuneSender.of(Bukkit.getPlayer(id));
    }

    public CommandSender get() {
        return this.player;
    }
}
