package com.dank1234.plugin.survival;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.command.Server;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.command.CommandSender;

@Cmd(
        server = Server.SURVIVAL,
        names = {"survival"},
        perms = {"admin"},
        disabled = false
)
public class SurvivalCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        Message.create(sender(), "Survival Command!").send();
    }
}
