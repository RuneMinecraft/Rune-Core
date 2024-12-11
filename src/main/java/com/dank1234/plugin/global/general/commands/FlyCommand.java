package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.command.CommandSender;

@Cmd(names="fly")
public class FlyCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        Message.create(User.of(sender.getName()), player().isFlying() ? "&cDisabled fly." : "&aEnabled fly.").send();
        player().setAllowFlight(!player().getAllowFlight());
    }
}
