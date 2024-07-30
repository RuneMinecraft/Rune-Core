package com.dank1234.plugin.global.playerdata;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.data.playerdata.PlayerDataManager;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.Messages;
import com.dank1234.utils.wrapper.player.RunePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Cmd(names="data")
public class DataCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Message.create(sender(), Messages.ARGUMENTS + "&c Usage: /data <player> <set | get>").send();
            return;
        }
        final Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            Message.create(sender(), Messages.NULL_PLAYER.toString()).send();
            return;
        }

        final PlayerDataManager pdm = RunePlayer.of(target).playerDataManager();
        pdm.loadData();

        if (super.checkArgument(1, "set")) {
            if (args.length != 4) {
                Message.create(sender(), Messages.ARGUMENTS + "&c Usage: /data <player> set <key> <value>").send();
                return;
            }
            String key = args[2];
            String value = args[3];

            pdm.setData(key, value);
            pdm.saveData();
            Message.create(sender(), "&aInserted new data, &f"+key+"&a:&f"+value).send();
        }
        else if (super.checkArgument(1, "get")) {
            if (args.length != 3) {
                Message.create(sender(), Messages.ARGUMENTS + "&c Usage: /data <player> get <key>").send();
                return;
            }
            String key = args[2];
            Message.create(sender(), "&aThe value for &f"+key+"&a is"+"&f "+pdm.getData(key)).send();
        }
    }
}
