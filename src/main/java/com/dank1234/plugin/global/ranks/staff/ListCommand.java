package com.dank1234.plugin.global.ranks.staff;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.data.database.StaffManager;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.staff.Staff;
import org.bukkit.command.CommandSender;

import java.util.Optional;

@Cmd(names="staff/list")
public class ListCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        Message.create(player(), "Staff List: ").send();
        for (Staff staff : StaffManager.getAll().get()) {
            Message.create(player(), staff.toString()).send();
        }
    }
}
