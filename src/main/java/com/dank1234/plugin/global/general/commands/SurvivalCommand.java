package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.server.ServerType;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

@Cmd(
        server = ServerType.GLOBAL,
        names = {"gms", "gmsurvival", "survival"},
        perms = {"admin"},
        disabled = false
)
public class SurvivalCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Message.create(player(), "&aSet your gamemode to &fSurvival&a.").send(false);
            User.of(player().getUniqueId()).setGameMode(GameMode.SURVIVAL);
            return;
        }

        User target = User.of(args(0));
        target.setGameMode(GameMode.SURVIVAL);
        Message.create(sender(), "&aSet &f"+target.username()+"'s&a gamemode to &aSurvival&a.").send(false);
    }
}
