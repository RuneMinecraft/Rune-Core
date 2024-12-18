package com.dank1234.api.command;

import com.dank1234.api.Utils;
import com.dank1234.api.wrapper.message.Message;
import com.dank1234.api.wrapper.message.Messages;
import com.dank1234.api.wrapper.player.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class ICommand implements Utils {
    public abstract void execute(@NotNull User user, @NotNull String ... args);

    private User user;

    private String[] names;
    private String[] args;

    public ICommand setSender(CommandSender sender) {
        this.user = User.of(sender.getName());
        return this;
    }

    public User sender() {
        return this.user;
    }
    public Player getPlayer() {
        return this.user.getPlayer();
    }

    public ICommand names(String[] names) {
        this.names = names;
        return this;
    }
    public ICommand names(int i, String name) {
        this.names[i] = name;
        return this;
    }
    public String[] names() {
        return this.names;
    }
    public String names(int i) {
        return this.names[i];
    }

    public ICommand args(String[] args) {
        this.args = args;
        return this;
    }
    public ICommand args(int i, String args) {
        this.args[i] = args;
        return this;
    }
    public String[] args() {
        return this.args;
    }
    public String args(int i) {
        return this.args[i];
    }

    public boolean disabled() {
        Command cmd = this.getClass().getAnnotation(Command.class);
        return cmd != null && cmd.disabled();
    }

    public boolean checkArgument(int i, String s) {
        if (i >= args.length) {
            Message.create(this.sender(), Messages.ARGUMENTS.toString()).send();
            return false;
        }
        return args[i].equalsIgnoreCase(s);
    }
}