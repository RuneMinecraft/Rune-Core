package com.dank1234.plugin.global.ranks.staff;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.ForceType;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.command.CommandSender;

@Cmd(names="staff/demote", perms="rune.staff.manager")
public class DemoteCommand extends ICommand {
    // Database staffDatabase = Database.of("staff");

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Message.create(player(), "&cInvalid Arguments! Usage: /demote <player>").send(false);
            return;
        }

        User target = User.of(args(0));
        if (target != null) {
            //if (!target.isStaff()) {
                Message.create(player(), "&cThat user is not staff!").send(false);
                return;
            //}
            //rPlayer().force(ForceType.COMMAND, "lp user "+target.username()+" parent cleartrack staff");
            //rPlayer().force(ForceType.COMMAND, "lp user "+target.username()+" permission unset rune.staff."+ StaffManager.getRank(target).name().toLowerCase());
        }else{
            rPlayer().force(ForceType.COMMAND, "say bye");
        }
    }
}