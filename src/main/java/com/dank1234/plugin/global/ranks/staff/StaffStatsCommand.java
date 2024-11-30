package com.dank1234.plugin.global.ranks.staff;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.data.database.StaffManager;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import com.dank1234.utils.wrapper.player.staff.Staff;
import org.bukkit.command.CommandSender;

@Cmd(names="staffstats")
public class StaffStatsCommand extends ICommand {
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

            Message.create(player(),
                    "&aStaff Stats for &f"+target.username()+"&a:",
                    "&a| &lRank&a: &r"+ staff.rank().rank.getCachedData().getMetaData().getPrefix(),
                    "&a| &lStaff Time&a: &f"+staff.time()+"&7&o*Formatting still needed*",
                    "&a| &lMessages&a: &f"+staff.messages(),
                    "&a| &lWarns&a: &f"+staff.warns(),
                    "&a| &lMutes&a: &f"+staff.mutes(),
                    "&a| &lBans&a: &f"+staff.bans()
            ).send();
        }
    }
}
