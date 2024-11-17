package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

@Cmd(names="rename")
public class RenameCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        ItemStack item = player().getActiveItem();
        item.getItemMeta().setDisplayName(Colour(args(0)));
    }
}
