package com.dank1234.utils.command;

import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.MessageType;
import com.dank1234.utils.Utils;
import com.dank1234.utils.wrapper.player.RPlayer;
import com.dank1234.utils.wrapper.player.RSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class ICommand implements Utils {
    public abstract void execute(CommandSender sender, String[] args);

    private CommandSender sender;
    private RSender rSender;
    private Player player;
    private RPlayer rPlayer;

    private String[] names;
    private String[] permissions;

    public void sender(CommandSender sender) {
        this.sender = sender;
        this.rSender = RSender.of(sender);
    }
    public CommandSender sender() {
        return this.sender;
    }
    public RSender rSender() {
        return this.rSender;
    }

    public void player(Player player) {
        this.player = player;
        this.rPlayer = RPlayer.of(player);
    }
    public Player player() {
        return this.player;
    }
    public RPlayer rPlayer(){
        return this.rPlayer;
    }

    public void names(String[] names) {
        this.names = names;
    }
    public void name(int i, String name) {
        this.names[i] = name;
    }
    public String[] names() {
        return this.names;
    }
    public String name(int i) {
        return this.names[i];
    }

    public void permissions(String[] perms) {
        this.permissions = perms;
    }
    public void permission(int i, String perm) {
        this.permissions[i] = perm;
    }
    public String[] permissions() {
        return this.permissions;
    }
    public String permission(int i) {
        return this.permissions[i];
    }

    public Player checkPlayer(Player player) {
        if (player == null) {
            Message.create(MessageType.ERROR, this.player, "Player parsing error!");
            return null;
        }
        return player;
    }
    public boolean isPlayer() {
        return player != null;
    }
}