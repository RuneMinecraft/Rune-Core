package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.item.Item;
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
        int amount = 1;
        String blockTag = args(0);

        Item item;
    }
}
