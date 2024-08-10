package com.dank1234.plugin.global.punishlite.commands;

import com.dank1234.plugin.global.punishlite.Punishment;
import com.dank1234.plugin.global.punishlite.PunishmentType;
import com.dank1234.plugin.global.punishlite.modifiers.Global;
import com.dank1234.plugin.global.punishlite.modifiers.Modifier;
import com.dank1234.plugin.global.punishlite.modifiers.Public;
import com.dank1234.plugin.global.punishlite.modifiers.Silent;
import com.dank1234.utils.DateUtils;
import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.players.User;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Cmd(
        names="mute", perms="punish.mute"
)
public class MuteCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Message.create(sender(), Messages.ARGUMENTS+" &cUsage: /mute <player> <length> <reason> <modifiers>").send(false);
            return;
        }

        User user = User.of(args(0));
        Player target = Bukkit.getPlayer(user.uuid());

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

        Punishment punishment = Punishment.of(PunishmentType.MUTE, user.uuid(),
                isPlayer() ? player().getUniqueId() : UUID.fromString("CONSOLE"),
                reason, 1,  modifiers.toArray(new Class[0]));

        punishment.punish();
        if (target != null) {
            Message.create(target, "&4You have been muted by "+punishment.staff()+".", "&4Expires in: "+ DateUtils.fromLong(punishment.punishmentLength()), "&4Reason: "+punishment.reason()).send(false);
        }
    }
}
