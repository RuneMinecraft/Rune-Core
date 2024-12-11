package com.dank1234.plugin.global.ranks.command;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;

import org.bukkit.command.CommandSender;

@Cmd(names = "track")
public class TrackCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return;
        }

    }
}
