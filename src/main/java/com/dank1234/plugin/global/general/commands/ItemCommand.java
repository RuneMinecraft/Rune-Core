package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.item.Items;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.command.CommandSender;

@Cmd(
        names = {"item", "i"},
        playerOnly = true
)
public class ItemCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return;
        }
        User.of(sender.getName()).giveItem(Items.EPIC_SWORD.getItem());
        User.of(sender.getName()).giveItem(Items.BACK_ARROW.getItem());
        User.of(sender.getName()).giveItem(Items.NEXT_ARROW.getItem());
    }
}
