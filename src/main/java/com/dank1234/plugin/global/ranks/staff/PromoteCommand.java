package com.dank1234.plugin.global.ranks.staff;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.data.database.StaffManager;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.staff.StaffRank;
import org.bukkit.command.CommandSender;

@Cmd(names = "staff/promote", perms = "rune.staff.manager")
public class PromoteCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        User target;
        if (User.of(args(0)) != null) {
            target = User.of(args(0));
        } else throw new IllegalStateException("User is null!");

        Staff staff;
        if (StaffManager.getStaff(target.getUuid()).isEmpty()) {
            staff = Staff.Companion.of(target.getUuid(), target.getUsername(), StaffRank.HELPER);
            StaffManager.insert(staff);
        } else {
            staff = Staff.Companion.of(target.getUuid());
            if (staff.rank().equals(StaffRank.MANAGER)) {
                Message.create(player(), "&cThat player is manager so cannot be promoted further.").send();
                return;
            }
            staff.setStaffMode(false);
            staff.setRank(StaffRank.getByOrdinal(staff.rank().ordinal() + 1));

            StaffManager.delete(staff.getUuid());
            StaffManager.insert(staff);

            staff.setStaffMode(true);
        }

        Message.create(player(), "&aPromoted &f"+args(0)+"&a to &f"+staff.rank().toString()+"&a.").send();
    }
}
