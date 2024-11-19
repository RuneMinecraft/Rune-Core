package com.dank1234.plugin.global.ranks.staff;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.command.CommandSender;

@Cmd(names="staffmode")
public class StaffModeCommand extends ICommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        User target = User.of(sender.getName());
        if (args.length >= 1 ||User.of(args(0)) != null) {
            // User is provided and assigned.
            target = User.of(args(0));
        }else throw new IllegalStateException("User is null!");


    }
}
