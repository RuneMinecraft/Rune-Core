package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Command;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

@Command(names="ping", playerOnly = true)
public class PingCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        final long millis = System.currentTimeMillis();
        user.sendMessage("&aPong! Your ping is: &f"+user.getPlayer().getPing()+"ms");
    }
}
