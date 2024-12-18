package com.dank1234.plugin.global.general.commands;

import com.dank1234.api.command.Command;
import com.dank1234.api.command.ICommand;
import com.dank1234.api.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

@Command(names="ping", playerOnly = true)
public class PingCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        user.sendMessage("&aPong! Your ping is: &f"+user.getPlayer().getPing()+"ms");
    }
}
