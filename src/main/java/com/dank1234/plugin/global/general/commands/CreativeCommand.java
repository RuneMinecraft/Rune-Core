package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.MessageType;
import com.dank1234.utils.wrapper.player.RunePlayer;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

@Cmd(
        names = {"gmc", "gmcreative", "creative"},
        perms = "gamemode.creative"
)
public class CreativeCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!isPlayer()) {
            Message.create(MessageType.ERROR, sender(), "&cCannot change your gamemode.").send(false);
            return;
        }
        if (args.length == 0) {
            Message.create(player(), "&aChanged your gamemode to creative.").send(false);
            rPlayer().gamemode(GameMode.CREATIVE);
            return;
        }

        RunePlayer target = RunePlayer.of(args[0]);
        target.gamemode(GameMode.CREATIVE);
        Message.create(sender(), "&aChanged &f"+target.player().getName()+"'s&a gamemode to creative.").send(false);
    }
}