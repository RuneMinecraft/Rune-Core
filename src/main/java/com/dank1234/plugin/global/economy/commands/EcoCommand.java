package com.dank1234.plugin.global.economy.commands;

import com.dank1234.plugin.global.economy.Economy;
import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.server.ServerType;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.message.Messages;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.command.CommandSender;

@Cmd(
        server = ServerType.GLOBAL,
        names = {"eco", "economy"},
        perms = "admin"
)
public class EcoCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length <= 2) {
            Message.create(sender(), Messages.ARGUMENTS.toString()).send(false);
            return;
        }

        if (super.checkArgument(1, "souls") || super.checkArgument(1, "tokens") || super.checkArgument(1, "gems")) {
            User target = User.of(args(0));
            if (target == null) {
                // TODO: write the fail instead of throwing an error please
                throw new IllegalStateException("User is null!");
            }

            Economy eco = Economy.getByName(args(1));
            String action = args(2);

            double amount = -1;
            if (args.length == 4) {
                amount = Double.parseDouble(args(3));
            }

            switch (action) {
                case "set":
                    target.setEco(eco, amount);
                    break;
                case "reset":
                    target.setEco(eco, 0);
                    break;
                case "get":
                    Message.create(player(), "&f" + target.username() + "&a has $&f" + target.getEco(eco) + "&a " + eco.getName() + ".").send();
                    return;
                case "give":
                    target.setEco(eco, target.getEco(eco) + amount);
                    break;
                default:
                    Message.create(player(), "&cInvalid action for economy command!").send();
                    return;
            }
            Message.create(player(), "&aSet &f" + target.username() + "'s &a" + eco.getName() + " balance to $&f" + target.getEco(eco) + "&a.").send();
        } else {
            //TODO: NOT VALID ECO MESSAGE :D
        }
    }
}
