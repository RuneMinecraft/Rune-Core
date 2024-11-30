package com.dank1234.plugin.global.spawn;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

@Cmd(names = "spawn", playerOnly = true)
public class SpawnCommand extends ICommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            player().teleport(Spawn.get().location());
            return;
        }
        if (super.checkArgument(0, "set")) {
            Location loc = player().getLocation();

            // Main.get().config().setValue("spawn.world", loc.getWorld().getName());

            // Main.get().config().setValue("spawn.x", loc.getX()+"");
            // Main.get().config().setValue("spawn.y", loc.getY()+"");
            // Main.get().config().setValue("spawn.z", loc.getZ()+"");

            // Main.get().config().setValue("spawn.yaw", loc.getYaw()+"");
            // Main.get().config().setValue("spawn.pitch", loc.getPitch()+"");

            Message.create(player(), "&6Set the spawn location to").send();
        }
    }
}
