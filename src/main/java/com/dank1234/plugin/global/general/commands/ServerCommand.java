package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.command.CommandSender;

@Cmd(names="server")
public class ServerCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Message.create(sender(), "Server Type: ").send(false);
            return;
        }
    }
}
