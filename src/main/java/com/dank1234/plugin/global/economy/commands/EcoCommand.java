package com.dank1234.plugin.global.economy.commands;

import com.dank1234.plugin.global.economy.Economy;
import com.dank1234.api.Locale;
import com.dank1234.api.command.Command;
import com.dank1234.api.command.ICommand;
import com.dank1234.api.server.ServerType;
import com.dank1234.api.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

@Command(
        server = ServerType.GLOBAL,
        names = {"eco", "economy"}
)
public class EcoCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        if (args.length <= 2) {
            user.sendMessage(Locale.INVALID_ARGUMENTS);
            return;
        }

        if (super.checkArgument(1, "souls") || super.checkArgument(1, "tokens") || super.checkArgument(1, "gems")) {
            User target = User.of(args(0));

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
                     user.sendMessage("&f" + target.getUsername() + "&a has $&f" + target.getEco(eco) + "&a " + eco.getName() + ".");
                    return;
                case "give":
                    target.setEco(eco, target.getEco(eco) + amount);
                    break;
                default:
                    user.sendMessage("&cInvalid action for economy command!");
                    return;
            }
            user.sendMessage("&aSet &f" + target.getUsername() + "'s &a" + eco.getName() + " balance to $&f" + target.getEco(eco) + "&a.");
        } else {
            //TODO: NOT VALID ECO MESSAGE :D
        }
    }
}
