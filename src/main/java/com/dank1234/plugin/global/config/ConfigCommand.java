package com.dank1234.plugin.global.config;

import com.dank1234.plugin.Main;
import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.server.ServerType;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Set;

@Cmd(
        server = ServerType.GLOBAL,
        names = "config",
        perms = "admin",
        disabled = false
)
public class ConfigCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            Message.create(sender(),
                    "&c<-- &6Config Help &c-->",
                    "&c| &e/config help",
                    "&c| &e/config set <key> <value>",
                    "&c| &e/config get <key>",
                    "&c| &e/config reload"
            ).send(false);
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            Set<CommandSender> messageRecipients = getOnlineOperators();
            messageRecipients.add(player() != null ? player() : Bukkit.getConsoleSender());

            Message.create(messageRecipients.toArray(CommandSender[]::new), "&eReloading config...").send(true);
            Main.get().reloadConfig();
            Message.create(messageRecipients.toArray(CommandSender[]::new), "&aReloaded config!").send(true);
            return;
        }

        if (args[0].equalsIgnoreCase("get")) {
            if (args.length != 2) {
                Message.create(sender(), Messages.ARGUMENTS+" &c/config get <key>").send(false);
                return;
            }

            String key = args[1];
            if (key != null) {
                Message.create(sender(), "&6Value: &e"+Main.get().config().getValue(key)).send(false);
            }
            return;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (args.length != 3) {
                Message.create(sender(), Messages.ARGUMENTS+" &c/config set <key> <value>").send(false);
                return;
            }

            String key = args[1];
            String value = args[2];
            if (key != null && value != null) {
                Main.get().config().setValue(key, value);
                Main.get().reloadConfig();
                Message.create(sender(), "&6Updated the value of &e"+key+"&6 to &e"+Main.get().config().getValue(key)).send(false);
            }
        }
    }
}
