package com.dank1234.plugin.global.ranks.staff;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.data.database.StaffManager;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import com.dank1234.utils.wrapper.player.staff.Staff;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@Cmd(names="staffmode")
public class StaffModeCommand extends ICommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        User target = User.of(sender.getName());
        if (args.length >= 1 && User.of(args(0)) != null) {
            target = User.of(args(0));
        }

        if (StaffManager.getStaff(target.uuid()).isEmpty()) {
            Message.create(player(), "&cYou are not a staff!").send();
        } else {
            Staff staff = Staff.of(target.uuid());
            staff.setStaffMode(!staff.staffMode());
            Message.create(player(), staff.staffMode() ? "&cDisabled staff mode." : "&aEnabled staff mode.").send();
        }
    }
}