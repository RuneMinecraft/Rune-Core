package com.dank1234.plugin.global.ranks.staff;

import com.dank1234.utils.RankUtils;
import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.data.database.StaffManager;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import com.dank1234.utils.wrapper.player.staff.Staff;
import com.dank1234.utils.wrapper.player.staff.StaffRank;
import org.bukkit.command.CommandSender;

@Cmd(names="staff/demote", perms="rune.staff.manager")
public class DemoteCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        User target;
        if (User.of(args(0)) != null) {
            // User is provided and assigned.
            target = User.of(args(0));
        } else throw new IllegalStateException("User is null!");

        if (StaffManager.getStaff(target.getUuid()).isEmpty()) {
            Message.create(player(), "&cThat player is not staff!").send();
        } else {
            Staff staff = Staff.Companion.of(target.getUuid());
            if (args.length != 2) {
                if (staff.rank().ordinal() == 0) {
                    StaffManager.delete(staff.getUuid());
                    Message.create(player(), "&cThat player cannot be demoted further. Removing staff").send(); // TODO: Write this idk
                    return;
                }
                StaffRank rank = StaffRank.getByOrdinal(staff.rank().ordinal()-1);

                StaffManager.delete(staff.getUuid());
                StaffManager.insert(staff);

                RankUtils.removeStaffTrack(User.of(staff.getUuid()));

                staff.setRank(rank);
                if (!staff.staffMode()) staff.setStaffMode(true);
                Message.create(player(), "&CDemoted &f"+target.getUsername()+"&c to &f"+rank.toString()+"&c.");
            }else if (args(1).equalsIgnoreCase("all")){
                staff.setStaffMode(false);
                StaffManager.delete(target.getUuid());
                Message.create(player(), "&cDemoted &f"+target.getUsername()+"&c to &fMEMBER").send();
            }
        }
    }
}