package com.dank1234.plugin.global.ranks.staff;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.data.database.StaffManager;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.ForceType;
import com.dank1234.utils.wrapper.player.User;
import com.dank1234.utils.wrapper.player.staff.Staff;
import com.dank1234.utils.wrapper.player.staff.StaffRank;
import org.bukkit.command.CommandSender;

import java.util.Objects;

@Cmd(names="staff/demote", perms="rune.staff.manager")
public class DemoteCommand extends ICommand {
    // Database staffDatabase = Database.of("staff");

    @Override
    public void execute(CommandSender sender, String[] args) {
        User target;
        if (User.of(args(0)) != null) {
            // User is provided and assigned.
            target = User.of(args(0));
        }else throw new IllegalStateException("User is null!");

        // Player is not staff.
        if (Staff.of(target.uuid()) != null) {
            // TODO: SEND MESSAGE
        } else { // Player is staff. Add the rank lower.
            Staff staff = Staff.of(target.uuid());
            if (args.length == 2) {
                StaffRank rank = StaffRank.valueOf(args(1).toUpperCase());
                if (staff.rank().equals(StaffRank.MANAGER)) {
                    // nice try pricks no demoting me - dan
                    // TODO: SEND MESSAGE
                    return;
                }
                if (staff.rank().ordinal() == 0) { // They are the lowest rank. Remove staff.
                    StaffManager.delete(staff.uuid());
                    // TODO: SEND MESSAGE
                    return;
                }
                staff.setRank(rank);
                StaffManager.delete(staff.uuid());
                StaffManager.insert(staff);
                // TODO: SEND MESSAGE
            }else{
                StaffManager.delete(target.uuid());
                // TODO: SEND MESSAGE
            }
        }
    }
}