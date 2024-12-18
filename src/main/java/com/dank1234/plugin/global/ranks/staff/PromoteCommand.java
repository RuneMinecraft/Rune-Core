package com.dank1234.plugin.global.ranks.staff;

import com.dank1234.utils.command.Command;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.data.database.StaffManager;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import com.dank1234.utils.wrapper.player.staff.Staff;
import com.dank1234.utils.wrapper.player.staff.StaffRank;
import org.jetbrains.annotations.NotNull;

@Command(names = "staff/promote")
public class PromoteCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        /*
        User target = User.of(args(0));

        Staff staff;
        if (StaffManager.getStaff(target.getUuid()).isEmpty()) {
            staff = Staff.Companion.of(target.getUuid(), target.getUsername(), StaffRank.HELPER);
            StaffManager.insert(staff);
        } else {
            staff = Staff.Companion.of(target.getUuid());
            if (staff.rank().equals(StaffRank.MANAGER)) {
                user.sendMessage("&cThat player is manager so cannot be promoted further.");
                return;
            }
            staff.setStaffMode(false);
            staff.setRank(StaffRank.getByOrdinal(staff.rank().ordinal() + 1));

            StaffManager.delete(staff.getUuid());
            StaffManager.insert(staff);

            staff.setStaffMode(true);
        }

        user.sendMessage("&aPromoted &f"+args(0)+"&a to &f"+ staff.rank() +"&a.");

         */
    }
}
