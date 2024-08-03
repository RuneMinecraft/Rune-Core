package com.dank1234.plugin.global.punishlite.commands;

import com.dank1234.plugin.global.punishlite.Punishment;
import com.dank1234.plugin.global.punishlite.PunishmentType;
import com.dank1234.plugin.global.punishlite.modifiers.Global;
import com.dank1234.plugin.global.punishlite.modifiers.Modifier;
import com.dank1234.plugin.global.punishlite.modifiers.Public;
import com.dank1234.plugin.global.punishlite.modifiers.Silent;
import com.dank1234.plugin.global.punishlite.players.User;
import com.dank1234.utils.DateUtils;
import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.data.punishments.UserManager;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.MessageType;
import com.dank1234.utils.wrapper.message.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

@Cmd(names="kick", perms="punish.kick")
public class KickCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Message.create(sender(), Messages.ARGUMENTS+" &cUsage: /kick <player> <reason> <modifiers>").send(false);
            return;
        }

        User user = User.of(args(0));
        if (Bukkit.getPlayer(user.uuid()) == null) {
            Message.create(MessageType.ERROR, "&cThat player is not online.").send(false);
            return;
        }
        String reason = null;
        List<Class<? extends Modifier>> modifiers = new ArrayList<>();

        StringBuilder reasonBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                char modifier = args[i].charAt(1);
                switch (modifier) {
                    case 's':
                        modifiers.add(Silent.class);
                        break;
                    case 'p':
                        modifiers.add(Public.class);
                    case 'g':
                        modifiers.add(Global.class);
                    default:
                        Message.create(sender(), Messages.ARGUMENTS + " &cUnknown modifier: " + args[i]).send(false);
                        return;
                }
            } else {
                if (!reasonBuilder.isEmpty()) {
                    reasonBuilder.append(" ");
                }
                reasonBuilder.append(args[i]);
            }
        }
        reason = reasonBuilder.toString();
        if (reason.equals(" ") || reason.isEmpty()) {
            reason = null;
        }

        Punishment punishment = Punishment.of(PunishmentType.KICK, user.uuid(),
                isPlayer() ? player().getUniqueId() : UUID.fromString("CONSOLE"),
                reason, -1L,  modifiers.toArray(new Class[0]));

        punishment.punish();
        this.kickPlayer(punishment, Bukkit.getPlayer(user.uuid()));
    }

    private void kickPlayer(Punishment punishment, Player target) {
        target.kickPlayer(Colour(
                "&cYou have been kicked!" + "\n&f" +
                        DateUtils.date(punishment.startTime()) + "\n\n" +
                        "&cStaff: &f"+UserManager.getUser(punishment.staff()).username()+"\n"+
                        "&cReason: &f"+punishment.reason()+"\n"+
                        "\n"+
                        "&7&o"+punishment.punishmentId()
        ));
    }
}
