package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Command;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.MessageType;
import com.dank1234.utils.wrapper.message.Messages;
import com.dank1234.utils.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Command(
        names = {"sudo"}
)
public class SudoCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        if (args.length <= 1) {
            Message.create(MessageType.ERROR, sender(), Messages.ARGUMENTS + " &cUsage: /sudo <target> <command>").send(false);
            return;
        }
        User target = User.of(args(0));
        boolean chat = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).startsWith("c://") ||
                String.join(" ", Arrays.copyOfRange(args, 1, args.length)).startsWith("C://");

        user.sendMessage("&eSudoing &f"+target.getUsername()+" &eto &e"+(chat
                ? "send the message &e'&f" + String.join(" ", Arrays.copyOfRange(args, 1, args.length)).substring(4)
                : "perform the command &e'&f" + String.join(" ", Arrays.copyOfRange(args, 1, args.length)))+"&e'.");
        target.sudo(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
    }
}
