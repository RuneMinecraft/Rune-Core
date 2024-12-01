package com.dank1234.plugin.global.world.commands;

import com.dank1234.plugin.Main;
import com.dank1234.plugin.global.world.generation.WorldGenerationType;
import com.dank1234.plugin.global.world.generation.WorldGenerator;
import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.io.File;

@Cmd(names="world")
public class WorldCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Message.create(sender, Colour("&cInvalid arguments! Usage: /world tp <world>")).send();
            return;
        }

        // /world <create|tp|delete|list> <world>
        String subCommand = args(0);
        switch (subCommand) {
            case "teleport", "tp" -> {
                if (args.length != 2) {
                    Message.create(sender, Colour("&cInvalid arguments! Usage: /world tp <world>")).send();
                    return;
                }

                World world = Bukkit.getWorld(args[1]);
                if (world == null) {
                    Message.create(sender, Colour("&cWorld not found: &f" + args[1])).send();
                    return;
                }

                player().teleport(new Location(world, 0, 64, 0));
                Message.create(sender, Colour("&aTeleported to world &f" + args[1] + "&a!")).send();
            }
            case "create" -> {
                if (args.length < 3) {
                    Message.create(sender, Colour("&cInvalid arguments! Usage: /world create <name> <type>")).send();
                    return;
                }

                String name = args[1];
                String typeString = args[2].toUpperCase();

                try {
                    WorldGenerationType type = WorldGenerationType.valueOf(typeString);
                    if (Bukkit.getWorld(name) != null) {
                        Message.create(sender, Colour("&cA world with the name &f" + name + " &calready exists!")).send();
                        return;
                    }

                    WorldGenerator.create(name, type);
                    Message.create(sender, Colour("&aSuccessfully created the world &f" + name + "&a with type &f" + type + "&a!")).send();
                } catch (IllegalArgumentException e) {
                    Message.create(sender, Colour("&cInvalid world type: &f" + typeString)).send();
                }
            }
            case "delete" -> {
                if (args.length != 2) {
                    Message.create(sender, Colour("&cInvalid arguments! Usage: /world delete <world>")).send();
                    return;
                }

                String worldName = args[1];
                World world = Bukkit.getWorld(worldName);

                if (world == null) {
                    Message.create(sender, Colour("&cWorld not found: &f" + worldName)).send();
                    return;
                }

                Bukkit.unloadWorld(world, false);
                Main.get().worlds().remove(worldName);
                File worldFolder = new File(Bukkit.getServer().getWorldContainer(), worldName);
                if (deleteDirectory(worldFolder)) {
                    Message.create(sender, Colour("&aSuccessfully deleted the world &f" + worldName + "&a!")).send();
                } else {
                    Message.create(sender, Colour("&cFailed to delete the world &f" + worldName + "&c!")).send();
                }
            }
            case "list" -> {
                Message.create(sender, "&aList of loaded worlds:").send();
                for (String worldName : Main.get().worlds()) {
                    System.out.println(worldName);
                    TextComponent clickableWorld = Component.text(worldName)
                            .color(NamedTextColor.WHITE)
                            .hoverEvent(HoverEvent.showText(Component.text(Colour("&aClick to teleport!"))))
                            .clickEvent(ClickEvent.runCommand("/world tp " + worldName));

                    Message.create(sender, Component.text(Colour("&a| ")).append(clickableWorld)).send();
                }
            }
            default -> {
                Message.create(sender, Colour("&cUnknown subcommand! Use /world <create|tp|delete|list>")).send();
            }
        }
    }

    private boolean deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!deleteDirectory(file)) {
                        return false;
                    }
                }
            }
        }
        return directory.delete();
    }
}
