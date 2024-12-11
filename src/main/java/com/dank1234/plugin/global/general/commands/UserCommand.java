package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.command.CommandSender;

@Cmd(names = "user", perms = "admin")
public class UserCommand extends ICommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        User user = User.of(sender.getName());
        Message.create(player(), user.toString()).send();
    }
}
