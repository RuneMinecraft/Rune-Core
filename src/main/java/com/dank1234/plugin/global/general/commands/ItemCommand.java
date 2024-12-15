package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Command;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.item.Items;
import com.dank1234.utils.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

@Command(
        names = {"item", "i"},
        playerOnly = true
)
public class ItemCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        if (args.length == 0) {
            return;
        }
        user.giveItem(Items.EPIC_SWORD.getItem());
        user.giveItem(Items.BACK_ARROW.getItem());
        user.giveItem(Items.NEXT_ARROW.getItem());
    }
}
