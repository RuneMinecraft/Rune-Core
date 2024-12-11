package com.dank1234.plugin.global.teleport;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.command.CommandSender;

@Cmd(names = {"tphere", "s"})
public class TpHereCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            Message.create(sender, "&cInvalid arguments! Usage: /<command> <player>").send();
            return;
        }

        User user = User.of(args(0));
        if (user == null) {
            Message.create(sender, "&cThat player is not in our database!").send();
            return;
        }

        Message.create(sender, "&eTeleporting...").send();
        user.getPlayer().teleport(player().getLocation());
    }
}
