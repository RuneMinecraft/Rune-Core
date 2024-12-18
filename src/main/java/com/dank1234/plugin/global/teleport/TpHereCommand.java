package com.dank1234.plugin.global.teleport;

import com.dank1234.api.Locale;
import com.dank1234.api.command.Command;
import com.dank1234.api.command.ICommand;
import com.dank1234.api.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Command(names = {"tphere", "s", "tp-here"})
public class TpHereCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        if (args.length != 1) {
            user.sendMessage(Locale.INVALID_ARGUMENTS+" &cUsage: /<command> <player>");
            return;
        }

        Optional<User> target = User.getUser(args(0));
        if (target.isEmpty()) {
            user.sendMessage(Locale.INCORRECT_USER);
            return;
        }

        target.get().sendMessage("&eTeleporting...");
        target.get().teleport(sender());
    }
}
