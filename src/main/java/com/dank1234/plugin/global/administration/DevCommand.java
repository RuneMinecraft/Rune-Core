package com.dank1234.plugin.global.administration;

import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.Messages;
import org.bukkit.command.CommandSender;

@Cmd(names="dev", perms="admin")
public class DevCommand extends ICommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (checkArgument(0, "command")) {
            if (args.length == 1) {
                Message.create("&cArguments: /dev command").send(false);
                return;
            }

        }
        else if (checkArgument(0, "event")) {

        }
        else {
            Message.create(sender(), Messages.ARGUMENTS.toString()).send(false);
        }
    }
}
