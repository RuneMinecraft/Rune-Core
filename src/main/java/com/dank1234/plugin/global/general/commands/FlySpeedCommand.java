package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.Locale;
import com.dank1234.utils.command.Command;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

@Command(names="flyspeed")
public class FlySpeedCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        if (args.length == 0) {
            user.sendMessage(Locale.INVALID_ARGUMENTS+" /flyspeed <speed>");
            return;
        }

        try {
            float speed = Float.parseFloat(args[0]);
            if (speed < 0 || speed > 10) {
                user.sendMessage("&cSpeed needs to be from 1-10!");
                return;
            }

            float scaledSpeed = speed / 10.0f;
            sender().getPlayer().setFlySpeed(scaledSpeed);
            user.sendMessage("&aSet fly speed to " + speed);
        }catch(NumberFormatException e) {
            user.sendMessage(Locale.EXCEPTION_THROWN+" &cIncorrect format. You need to enter a number!");
        }
    }
}
