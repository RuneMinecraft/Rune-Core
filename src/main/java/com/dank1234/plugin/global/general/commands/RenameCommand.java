package com.dank1234.plugin.global.general.commands;

import com.dank1234.api.Locale;
import com.dank1234.api.command.Command;
import com.dank1234.api.command.ICommand;
import com.dank1234.api.wrapper.item.Item;
import com.dank1234.api.wrapper.player.User;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

@Command(names="rename")
public class RenameCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        // TODO: Permission Check! (rune.admin.rename)

        if (args.length == 0) {
            user.sendMessage(Locale.INVALID_ARGUMENTS+" /rename <name>.");
            return;
        }

        if (user.getHeldItem() == null || user.getHeldItem().material() == Material.AIR) {
            user.sendMessage("&cYou must be holding an item to rename!");
            return;
        }

        Item item = user.getHeldItem();
        item.setDisplayName(Colour(String.join(" ", args())));
    }
}
