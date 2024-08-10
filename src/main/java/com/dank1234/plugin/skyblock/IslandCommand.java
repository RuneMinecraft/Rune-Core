package com.dank1234.plugin.skyblock;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.server.ServerType;
import com.dank1234.utils.wrapper.message.Message;

import org.bukkit.command.CommandSender;

import java.sql.SQLException;
import java.util.Optional;

@Cmd(server = ServerType.SKYBLOCK, names="island")
public class IslandCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        final Island island = IslandUtils.getIslandByPlayer(player().getUniqueId())
                .or(() -> Optional.of(Island.create(player().getUniqueId())))
                .orElseGet(() -> {
                    Message.create("&cYou are not on an island!").send();
                    return null;
                });

        Message.create("Island Coords: "+island.grid()).send();
    }
}
