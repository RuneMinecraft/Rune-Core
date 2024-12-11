package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;

@Cmd(
        names = {"gmc", "gmcreative", "creative"},
        perms = "gamemode.creative",
        playerOnly = true
)
public class CreativeCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Message.create(player(), "&aSet your gamemode to &fCreative&a.").send(false);
            User.of(player().getUniqueId()).setGameMode(GameMode.CREATIVE);
            return;
        }

        User target = User.of(args(0));
        target.setGameMode(GameMode.CREATIVE);
        Message.create(sender(), "&aSet &f"+target.getUsername()+"'s&a gamemode to &aCreative&a.").send(false);
    }
}