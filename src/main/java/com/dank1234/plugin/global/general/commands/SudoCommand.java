package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.MessageType;
import com.dank1234.utils.wrapper.message.Messages;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

@Cmd(names = {"sudo"}, perms = {"admin"})
public class SudoCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            Message.create(MessageType.ERROR, sender(), Messages.ARGUMENTS + " &cUsage: /sudo <target> <command>").send(false);
            return;
        }
        User target = User.of(args(0));
        boolean chat = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).startsWith("c://") ||
                String.join(" ", Arrays.copyOfRange(args, 1, args.length)).startsWith("C://");

        Message.create(player(), "&eSudoing &f"+target.username()+" &eto &e"+(chat
                ? "send the message &e'&f" + String.join(" ", Arrays.copyOfRange(args, 1, args.length)).substring(4)
                : "perform the command &e'&f" + String.join(" ", Arrays.copyOfRange(args, 1, args.length)))+"&e'.").send();
        target.sudo(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
    }
}
