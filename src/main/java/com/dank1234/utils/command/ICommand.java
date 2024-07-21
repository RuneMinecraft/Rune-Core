package com.dank1234.utils.command;

import com.dank1234.utils.MessageType;
import com.dank1234.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class ICommand implements Utils {
    public abstract void execute(CommandSender sender, String[] args);

    private Player player;

    public void setPlayer(Player player) {
        this.player = player;
    }
    public Player getPlayer() {
        return player;
    }

    public void sendMessage(String message) {
        this.sendMessage(MessageType.NORMAL, message);
    }
    public void sendMessage(CommandSender sender, String message) {
        this.sendMessage(MessageType.NORMAL, sender, message);
    }
    public void sendMessage(MessageType type, String message) {
        this.sendMessage(type, getPlayer(), message);
    }
    public void sendMessage(MessageType type, CommandSender sender, String message) {
        sender.sendMessage(Colour(type + message));
    }

    public Player checkPlayer(Player player) {
        if (player == null) {
            sendMessage(MessageType.ERROR, "Player parsing error!");
            return null;
        }
        return player;
    }
    public boolean isPlayer() {
        return player != null;
    }
}