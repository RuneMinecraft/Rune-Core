package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.command.CommandSender;

@Cmd(names="flyspeed")
public class FlySpeedCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Message.create(User.of(sender.getName()), "§cUsage: /fly <speed>").send();
            return;
        }

        try {
            float speed = Float.parseFloat(args[0]);
            if (speed < 0 || speed > 10) {
                //TODO: do this please
                //Message.create()
                return;
            }

            float scaledSpeed = speed / 10.0f;
            player().setFlySpeed(scaledSpeed);
            Message.create(User.of(sender.getName()), "&aSet fly speed to " + speed).send();
        }catch(Exception ignore) {}
    }
}
