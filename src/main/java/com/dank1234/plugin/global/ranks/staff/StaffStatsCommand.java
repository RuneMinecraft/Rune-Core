package com.dank1234.plugin.global.ranks.staff;

import com.dank1234.api.command.Command;
import com.dank1234.api.command.ICommand;
import com.dank1234.api.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

@Command(names="staffstats")
public class StaffStatsCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        /*
        User target = user;
        if (args.length >= 1 && User.of(args(0)) != null) {
            target = User.of(args(0));
        }

        if (StaffManager.getStaff(target.getUuid()).isEmpty()) {
            user.sendMessage("&cYou are not a staff!");
        } else {
            Staff staff = Staff.Companion.of(target.getUuid());

            user.sendMessage(
                    "&aStaff Stats for &f"+target.getUsername()+"&a:",
                    "&a| &lRank&a: &r"+ staff.rank().rank.getCachedData().getMetaData().getPrefix(),
                    "&a| &lStaff Time&a: &f"+staff.time()+"&7&o*Formatting still needed*",
                    "&a| &lMessages&a: &f"+staff.messages(),
                    "&a| &lWarns&a: &f"+staff.warns(),
                    "&a| &lMutes&a: &f"+staff.mutes(),
                    "&a| &lBans&a: &f"+staff.bans()
            );
        }

         */
    }
}
