package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.server.ServerType;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.command.CommandSender;

@Cmd(
        server = ServerType.SURVIVAL,
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
