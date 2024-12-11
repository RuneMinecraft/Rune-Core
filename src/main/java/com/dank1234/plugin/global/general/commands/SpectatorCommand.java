package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

@Cmd(
        names = {"gmsp", "gmspectator", "spectator"},
        perms = "gamemode.spectator",
        playerOnly = true
)
public class SpectatorCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Message.create(player(), "&aSet your gamemode to &fSpectator&a.").send(false);
            User.of(player().getUniqueId()).setGameMode(GameMode.SPECTATOR);
            return;
        }

        User target = User.of(args(0));
        target.setGameMode(GameMode.SPECTATOR);
        Message.create(sender(), "&aSet &f"+target.getUsername()+"'s&a gamemode to &aSpectator&a.").send(false);
    }
}