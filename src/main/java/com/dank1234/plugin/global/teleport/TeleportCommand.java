package com.dank1234.plugin.global.teleport;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Cmd(names={"teleport", "tp", "tele"}, playerOnly = true)
public class TeleportCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        try {
            if (args.length == 1) {
                // /tp <target>
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    Message.create(sender,"&cThat player is not online!").send();
                    return;
                }

                player.teleport(target);
                Message.create(sender,"&eTeleporting to &f"+target.getName()+"&e...").send();
            } else if (args.length == 2) {
                // /tp <player1> <target>
                Player player1 = Bukkit.getPlayer(args[0]);
                Player target = Bukkit.getPlayer(args[1]);

                if (player1 == null) {
                    Message.create(sender,"&cThe player &c"+args(0)+"&c is not online!").send();
                    return;
                }
                if (target == null) {
                    Message.create(sender,"&cThe player &c"+args(1)+"&c is not online!").send();
                    return;
                }

                player1.teleport(target);
                Message.create(sender,"&eTeleporting &f"+player1.getName()+"&e to &f"+target.getName()+"&e...").send();
                if (sender != player1) Message.create(player1,"&eTeleporting to &f"+target.getName()+"&e...").send();
            } else if (args.length == 3) {
                // /tp <x> <y> <z>
                double x = Double.parseDouble(args[0]);
                double y = Double.parseDouble(args[1]);
                double z = Double.parseDouble(args[2]);

                Location location = new Location(player.getWorld(), x, y, z);
                player.teleport(location);
                Message.create(sender,"&eTeleporting to &f["+x+", "+y+", "+z+"]&e...").send();
            } else if (args.length == 4) {
                // /tp <player1> <x> <y> <z>
                Player player1 = Bukkit.getPlayer(args[0]);
                if (player1 == null) {
                    Message.create(sender,"&cThe player &c"+args(0)+"&c is not online!").send();
                    return;
                }

                double x = Double.parseDouble(args[1]);
                double y = Double.parseDouble(args[2]);
                double z = Double.parseDouble(args[3]);

                Location location = new Location(player1.getWorld(), x, y, z);
                player1.teleport(location);
                player.sendMessage(player1.getName() + " teleported to coordinates: " + x + ", " + y + ", " + z + ".");
                Message.create(sender,"&eTeleporting &f"+player1+" to &f["+x+", "+y+", "+z+"]&e...").send();
                if (sender != player1) Message.create(player1,"&eTeleporting you to &f["+x+", "+y+", "+z+"]&e...").send();
            } else {
                player.sendMessage("Invalid command usage. Try: /tp <target>, /tp <player1> <target>, /tp <x> <y> <z>, or /tp <player1> <x> <y> <z>.");
            }
        } catch (NumberFormatException e) {
            player.sendMessage("Coordinates must be valid numbers.");
        }
    }
}