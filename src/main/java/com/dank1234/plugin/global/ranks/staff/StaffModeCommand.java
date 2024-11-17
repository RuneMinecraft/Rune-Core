package com.dank1234.plugin.global.ranks.staff;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.command.CommandSender;

@Cmd(names="staffmode")
public class StaffModeCommand extends ICommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        Message.create().send();
    }
}
