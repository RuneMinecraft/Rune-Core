package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.command.CommandSender;

@Cmd(names="ping", playerOnly = true)
public class PingCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        long millis = System.currentTimeMillis();
        Message.create(sender, "&aPong! Your ping is: &f"+player().getPing()+"ms").send();
    }
}
