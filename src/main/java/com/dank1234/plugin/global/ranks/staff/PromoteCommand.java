package com.dank1234.plugin.global.ranks.staff;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.data.database.StaffManager;
import com.dank1234.utils.data.database.UserManager;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import com.dank1234.utils.wrapper.player.staff.Staff;
import com.dank1234.utils.wrapper.player.staff.StaffRank;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Cmd(names = "staff/promote", perms = "rune.staff.manager")
public class PromoteCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        User target;
        if (UserManager.getUser(args(0)).isPresent()) {
            // User is provided and assigned.
            target = UserManager.getUser(args(0)).get();
        }else throw new IllegalStateException("User is null!");

        // Player is not staff. Add HELPER.
        if (StaffManager.getStaff(target.uuid()).isEmpty()) {
            Staff newStaff = Staff.of(target.uuid(), target.username(), StaffRank.HELPER);
            StaffManager.insert(newStaff);
            // TODO: SEND MESSAGE
        } else { // Player is staff. Add the rank higher.
            Staff staff = Staff.of(target.uuid());
            if (staff.rank().equals(StaffRank.MANAGER)) {
                // THEY ARE MANAGER / CAN'T BE PROMOTED
                // TODO: SEND MESSAGE
                return;
            }
            staff.setRank(StaffRank.getByOrdinal(staff.rank().ordinal() + 1));
            // TODO: SEND MESSAGE
        }
    }
}
