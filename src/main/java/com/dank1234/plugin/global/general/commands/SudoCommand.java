package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.server.ServerType;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.MessageType;
import com.dank1234.utils.wrapper.message.Messages;
import com.dank1234.utils.wrapper.player.ForceType;
import com.dank1234.utils.wrapper.player.RPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@Cmd(
        server = ServerType.GLOBAL,
        names = {"sudo"},
        perms = {"admin"},
        disabled = false
)
public class SudoCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            Message.create(MessageType.ERROR, sender(), Messages.ARGUMENTS+" &cUsage: /sudo <target> <command | message>").send(false);
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        RPlayer rTarget = RPlayer.of(target);

        String s = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        if (s.startsWith("c://")) {
            s = s.substring(4);
        }

        rTarget.force(args[1].startsWith("c://") ? ForceType.CHAT : ForceType.COMMAND, s);
    }
}
