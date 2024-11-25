package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.command.CommandSender;

@Cmd(names = "discord")
public class DiscordCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        Message.create(player(), "&6RuneMC Discord: &chttps://discord.runemc.net/").send(false);
    }
}
