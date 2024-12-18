package com.dank1234.plugin.global.teleport;

import com.dank1234.api.Locale;
import com.dank1234.api.command.Command;
import com.dank1234.api.command.ICommand;
import com.dank1234.api.wrapper.location.Location;
import com.dank1234.api.wrapper.message.Message;
import com.dank1234.api.wrapper.player.User;

import org.jetbrains.annotations.NotNull;
import java.util.Optional;

@Command(names={"teleport", "tp", "tele"}, playerOnly = true)
public class TeleportCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        try {
            if (args.length == 1) {
                // /tp <target>
                Optional<User> target = User.getUser(args[0]);
                if (target.isEmpty()) {
                    user.sendMessage(Locale.INCORRECT_USER+" &c[&f"+args[0]+"&c]");
                    return;
                }

                user.teleport(target.get());
                user.sendMessage("&eTeleporting to &f"+target.get().getUsername()+"&e...");
            } else if (args.length == 2) {
                // /tp <player1> <target>
                Optional<User> player = User.getUser(args[0]);
                Optional<User> target = User.getUser(args[1]);

                if (player.isEmpty()) {
                    user.sendMessage(Locale.INCORRECT_USER+" &c[&f"+args[0]+"&c]");
                    return;
                }
                if (target.isEmpty()) {
                    user.sendMessage(Locale.INCORRECT_USER+" &c[&f"+args[1]+"&c]");
                    return;
                }

                player.get().teleport(target.get());
                user.sendMessage("&eTeleporting &f"+player.get().getUsername()+"&e to &f"+target.get().getUsername()+"&e...");
                if (user != player.get()) {
                    player.get().sendMessage("&eTeleporting to &f"+target.get().getUsername()+"&e...");
                }
            } else if (args.length == 3) {
                // /tp <x> <y> <z>
                double x = Double.parseDouble(args[0]);
                double y = Double.parseDouble(args[1]);
                double z = Double.parseDouble(args[2]);

                Location location = new Location(
                        user.getLocation().getWorld(),
                        x, y, z,
                        user.getLocation().getYaw(),
                        user.getLocation().getPitch()
                );
                user.teleport(location);
                user.sendMessage("&eTeleporting to &f["+x+", "+y+", "+z+"]&e...");
            } else if (args.length == 4) {
                // /tp <player1> <x> <y> <z>
                Optional<User> player = User.getUser(args[0]);
                if (player.isEmpty()) {
                    user.sendMessage(Locale.INCORRECT_USER+" &c[&f"+args[0]+"&c]");
                    return;
                }

                double x = Double.parseDouble(args[1]);
                double y = Double.parseDouble(args[2]);
                double z = Double.parseDouble(args[3]);

                Location location = new Location(
                        player.get().getLocation().getWorld(),
                        x, y, z,
                        player.get().getLocation().getYaw(),
                        player.get().getLocation().getPitch()
                );
                player.get().teleport(location);

                Message.create(user,"&eTeleporting &f"+player.get()+" to &f["+x+", "+y+", "+z+"]&e...").send();
                if (user != player.get()) {
                    player.get().sendMessage("&eTeleporting you to &f["+x+", "+y+", "+z+"]&e...");
                }
            } else {
                user.sendMessage(Locale.INVALID_ARGUMENTS);
            }
        } catch (NumberFormatException e) {
            user.sendMessage(Locale.INVALID_NUMBER_FORMAT);
        }
    }
}