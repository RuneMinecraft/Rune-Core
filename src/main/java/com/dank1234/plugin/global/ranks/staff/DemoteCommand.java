package com.dank1234.plugin.global.ranks.staff;

import com.dank1234.api.command.Command;
import com.dank1234.api.command.ICommand;
import com.dank1234.api.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

@Command(names="staff/demote")
public class DemoteCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        /*
        User target = User.of(args(0));

        if (StaffManager.getStaff(target.getUuid()).isEmpty()) {
             user.sendMessage("&cThat player is not staff!");
        } else {
            Staff staff = Staff.Companion.of(target.getUuid());
            if (args.length != 2) {
                if (staff.rank().ordinal() == 0) {
                    StaffManager.delete(staff.getUuid());
                    user.sendMessage("&cThat player cannot be demoted further. Removing staff"); // TODO: Write this idk
                    return;
                }
                StaffRank rank = StaffRank.getByOrdinal(staff.rank().ordinal()-1);

                StaffManager.delete(staff.getUuid());
                StaffManager.insert(staff);

                RankUtils.removeStaffTrack(User.of(staff.getUuid()));

                staff.setRank(rank);
                if (!staff.staffMode()) staff.setStaffMode(true);
                user.sendMessage("&CDemoted &f"+target.getUsername()+"&c to &f"+ rank +"&c.");
            }else if (args(1).equalsIgnoreCase("all")){
                staff.setStaffMode(false);
                StaffManager.delete(target.getUuid());
                 user.sendMessage("&cDemoted &f"+target.getUsername()+"&c to &fMEMBER");
            }
        }

         */
    }
}