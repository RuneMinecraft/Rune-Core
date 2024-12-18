package com.dank1234.plugin.global.ranks.staff;

import com.dank1234.utils.command.Command;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.player.User;
import com.dank1234.utils.wrapper.player.staff.Staff;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Command(names="staffmode")
public class StaffModeCommand extends ICommand {

    @Override
    public void execute(@NotNull User user, String[] args) {
        User target = user;
        if (args.length >= 1) {
            target = User.of(args(0));
        }
        Optional<Staff> optionalStaff = Staff.getStaff(target.getUuid());

        if (optionalStaff.isEmpty()) {
            user.sendMessage("&cYou are not a staff!");
        } else {
            Staff staff = optionalStaff.get();
            staff.toggleStaffMode();
            user.sendMessage(!staff.getStaffMode() ? "&cDisabled staff mode." : "&aEnabled staff mode.");
        }
    }
}