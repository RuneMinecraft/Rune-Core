package com.dank1234.plugin.skyblock;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.server.ServerType;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

@Cmd(server = ServerType.SKYBLOCK, names="island")
public class IslandCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        Island island = Island.create(player());
        Message.create(sender(), island.toString(), island.grid().toString()).send(true);

        System.out.println(island.blockList());
        for (Block block : island.blockList()) {
            block.setType(Material.STONE);
            Message.create("Set block to stone at: "+block.getLocation()).send(false);
        }
    }
}
