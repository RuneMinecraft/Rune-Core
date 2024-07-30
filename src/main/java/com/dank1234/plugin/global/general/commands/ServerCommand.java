package com.dank1234.plugin.global.general.commands;

import com.dank1234.plugin.Main;
import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.data.Config;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.MessageType;
import com.dank1234.utils.wrapper.message.Messages;
import org.bukkit.command.CommandSender;

@Cmd(names="server")
public class ServerCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            Message.create(sender(),
                    "&c<-- &6Server Info &c-->",
                    "&c| &6Type: &e"+ Main.get().server().TYPE(),
                    "&c| &6Release: &e"+ Main.get().server().RELEASE(),
                    "&c| &6Description: &e"+ Main.get().server().DESCRIPTION()
            ).send(false);
            return;
        }

        if (super.checkArgument(0, "help")) {
            Message.create(sender(), "&cUsage: /server <set> <type | release | description> <value>").send(false);
            return;
        }

        if (super.checkArgument(0, "set")) {
            if (args.length == 3) {
                Message.create(sender(), Messages.ARGUMENTS+"&cUsage: /server set <type | release | description> <value>").send(false);
                return;
            }

            if (!(super.checkArgument(1, "type") || super.checkArgument(1, "release") || super.checkArgument(1, "description"))) {
                Message.create(sender(), Messages.ARGUMENTS+"&cPlease either pick 'type', 'release' or 'description'!").send(false);
                return;
            }
            String info = args[1];
            String value = args[2];

            try {
                Main.get().config().setValue("server." + info, value);
                Message.create(sender(), "&aSuccessfully changed the server info!").send(false);
                Message.create(sender(),
                        "&c<-- &6Server Info &c-->",
                        "&c| &6Type: &e" + Main.get().server().TYPE(),
                        "&c| &6Release: &e" + Main.get().server().RELEASE(),
                        "&c| &6Description: &e" + Main.get().server().DESCRIPTION()
                ).send(false);
            }catch(Exception e) {
                e.printStackTrace();
                Message.create(MessageType.ERROR, sender(), "&cFailed to change the server info. Please check console for the error!").send(false);
            }
        }
    }
}
