package com.dank1234.plugin.global.teleport;

import com.dank1234.utils.command.Command;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Command(names={"teleport", "tp", "tele"}, playerOnly = true)
public class TeleportCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        Player player = (Player) user;

        try {
            if (args.length == 1) {
                // /tp <target>
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    Message.create(user,"&cThat player is not online!").send();
                    return;
                }

                player.teleport(target);
                Message.create(user,"&eTeleporting to &f"+target.getName()+"&e...").send();
            } else if (args.length == 2) {
                // /tp <player1> <target>
                Player player1 = Bukkit.getPlayer(args[0]);
                Player target = Bukkit.getPlayer(args[1]);

                if (player1 == null) {
                    Message.create(user,"&cThe player &c"+args(0)+"&c is not online!").send();
                    return;
                }
                if (target == null) {
                    Message.create(user,"&cThe player &c"+args(1)+"&c is not online!").send();
                    return;
                }

                player1.teleport(target);
                Message.create(user,"&eTeleporting &f"+player1.getName()+"&e to &f"+target.getName()+"&e...").send();
                if (user != player1) Message.create(player1,"&eTeleporting to &f"+target.getName()+"&e...").send();
            } else if (args.length == 3) {
                // /tp <x> <y> <z>
                double x = Double.parseDouble(args[0]);
                double y = Double.parseDouble(args[1]);
                double z = Double.parseDouble(args[2]);

                Location location = new Location(player.getWorld(), x, y, z);
                player.teleport(location);
                Message.create(user,"&eTeleporting to &f["+x+", "+y+", "+z+"]&e...").send();
            } else if (args.length == 4) {
                // /tp <player1> <x> <y> <z>
                Player player1 = Bukkit.getPlayer(args[0]);
                if (player1 == null) {
                    Message.create(user,"&cThe player &c"+args(0)+"&c is not online!").send();
                    return;
                }

                double x = Double.parseDouble(args[1]);
                double y = Double.parseDouble(args[2]);
                double z = Double.parseDouble(args[3]);

                Location location = new Location(player1.getWorld(), x, y, z);
                player1.teleport(location);
                player.sendMessage(player1.getName() + " teleported to coordinates: " + x + ", " + y + ", " + z + ".");
                Message.create(user,"&eTeleporting &f"+player1+" to &f["+x+", "+y+", "+z+"]&e...").send();
                if (user != player1) Message.create(player1,"&eTeleporting you to &f["+x+", "+y+", "+z+"]&e...").send();
            } else {
                player.sendMessage("Invalid command usage. Try: /tp <target>, /tp <player1> <target>, /tp <x> <y> <z>, or /tp <player1> <x> <y> <z>.");
            }
        } catch (NumberFormatException e) {
            player.sendMessage("Coordinates must be valid numbers.");
        }
    }
}