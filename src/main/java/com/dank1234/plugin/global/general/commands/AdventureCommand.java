package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

@Cmd(
        names = {"gma", "gmadventure", "adventure"},
        perms = "gamemode.adventure",
        playerOnly = true
)
public class AdventureCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Message.create(player(), "&aSet your gamemode to &fAdventure&a.").send(false);
            User.of(player().getUniqueId()).setGameMode(GameMode.ADVENTURE);
            return;
        }

        User target = User.of(args(0));
        target.setGameMode(GameMode.ADVENTURE);
        Message.create(sender(), "&aSet &f"+target.username()+"'s&a gamemode to &aAdventure&a.").send(false);
    }
}