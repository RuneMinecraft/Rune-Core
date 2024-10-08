package com.dank1234.plugin.survival.economy.commands;

import com.dank1234.plugin.survival.economy.Currency;
import com.dank1234.plugin.survival.economy.Souls;
import com.dank1234.plugin.survival.economy.Tokens;
import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.server.ServerType;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.MessageType;
import com.dank1234.utils.wrapper.message.Messages;
import com.dank1234.utils.wrapper.player.RunePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

@Cmd(
        server = ServerType.SURVIVAL,
        names = {"eco", "economy"},
        perms = "admin"
)
public class EcoCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            if (args.length <= 2) {
                Message.create(sender(), Messages.ARGUMENTS+" &cUsage: /eco <player> <souls | tokens> <set | get | reset | give> <amount>").send(false);
                return;
            }

            Player target = Bukkit.getPlayer(args[0]);
            RunePlayer runeTarget = RunePlayer.of(target);

            if (super.checkArgument(1, "souls") || super.checkArgument(1, "tokens")) {
                Class<? extends Currency> type = switch (args(1)) {
                    case "souls" -> Souls.class;
                    case "tokens" -> Tokens.class;
                    default -> null;
                };

                if (type == null) {
                    Message.create(MessageType.ERROR, sender(), "&cPlease choose either 'tokens' or 'souls'.").send(false);
                    return;
                }
                Currency superType = type.getDeclaredConstructor().newInstance();

                String action = args(2);
                double amount = -1;
                if (args.length == 4) {
                    amount = Double.parseDouble(args(3));
                }

                double balance = Double.parseDouble(Objects.requireNonNull(runeTarget.playerDataManager().getData("eco." + superType.getName())).value());

                switch (action) {
                    case "set":
                        runeTarget.playerDataManager().setData("eco."+superType.getName(), amount);
                        runeTarget.playerDataManager().saveData();
                        break;
                    case "get":
                        Message.create(sender(), "&aYour &"+superType.getColour()+superType.getName()+"&a balance is &e"+superType.getSymbol()+balance+"&a.").send(false);
                        break;
                    case "reset":
                        runeTarget.playerDataManager().setData("eco."+superType.getName(), 0);
                        runeTarget.playerDataManager().saveData();
                        break;
                    case "give":
                        runeTarget.playerDataManager().setData("eco."+superType.getName(), amount+balance);
                        runeTarget.playerDataManager().saveData();
                        break;
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
