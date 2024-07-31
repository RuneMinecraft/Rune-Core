package com.dank1234.plugin.survival.economy.commands;

import com.dank1234.plugin.survival.economy.Currency;
import com.dank1234.plugin.survival.economy.Souls;
import com.dank1234.plugin.survival.economy.Tokens;
import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.data.playerdata.PlayerDataManager;
import com.dank1234.utils.server.ServerType;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.MessageType;
import com.dank1234.utils.wrapper.message.Messages;
import com.dank1234.utils.wrapper.player.RunePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Cmd(
        server = ServerType.SURVIVAL,
        names = {"eco", "economy"},
        perms = "admin"
)
public class EcoCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            if (args.length <= 3) {
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

                String action = args(2);
                int amount = -1;
                if (args.length == 4) {
                    amount = Integer.parseInt(args(3));
                }

                switch (action) {
                    case "set":
                            runeTarget.playerDataManager().setData("eco."+type.newInstance().getName(), amount);
                            runeTarget.playerDataManager().saveData();
                        break;
                    case "get":
                        break;
                    case "reset":
                        break;
                    case "give":
                        break;
                }
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
