package com.dank1234.plugin.global.teleport;

import com.dank1234.utils.Locale;
import com.dank1234.utils.command.Command;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
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
