package com.dank1234.plugin.global.general.commands;

import com.dank1234.api.Locale;
import com.dank1234.api.Result;
import com.dank1234.api.command.Command;
import com.dank1234.api.command.ICommand;
import com.dank1234.api.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Command(
        names = {"sudo"}
)
public class SudoCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        // TODO: Permission Check! (rune.admin.sudo)

        if (args.length <= 1) {
            user.sendMessage(Locale.INVALID_ARGUMENTS+" &c/sudo <target> <command>");
            return;
        }
        User target = User.of(args(0));
        if (user.getPrimaryGroup().getWeight() > target.getPrimaryGroup().getWeight()) {
            user.sendMessage("&cYou cannot sudo that user!");
            return;
        }

        boolean isChat = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).startsWith("c://") ||
                String.join(" ", Arrays.copyOfRange(args, 1, args.length)).startsWith("C://");

        Result result = target.sudo(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
        switch (result) {
            case SUCCESSFUL -> user.sendMessage("&eForced &f"+target.getUsername()+" &eto &e"+(isChat
                    ? "send the message &e'&f" + String.join(" ", Arrays.copyOfRange(args, 1, args.length)).substring(4)
                    : "perform the command &e'&f" + String.join(" ", Arrays.copyOfRange(args, 1, args.length)))+"&e'.");
            case EXCEPTION -> user.sendMessage(Locale.EXCEPTION_THROWN);
        }
    }
}
