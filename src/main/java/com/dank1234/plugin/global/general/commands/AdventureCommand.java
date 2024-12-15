package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Command;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.player.GameMode;
import com.dank1234.utils.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Command(
        names = {"gma", "gmadventure", "adventure"},
        playerOnly = true
)
public class AdventureCommand extends ICommand {
    private final GameMode gameMode = GameMode.ADVENTURE;

    @Override
    public void execute(@NotNull User user, String[] args) {
        if (args.length == 0) {
            user.sendMessage("&aSet your gamemode to &f"+gameMode.getName()+"&a.");
            user.setGameMode(this.gameMode);
            return;
        }

        Optional<User> optionalUser = User.getUser(args(0));
        if (optionalUser.isEmpty()) {
            user.sendMessage("&cThe player &f"+args(0)+"&c does not exist in our database.");
            return;
        }

        User target = optionalUser.get();
        if (target.isOnline()) {
            target.setGameMode(this.gameMode);
            user.sendMessage("&aSet &f" + target.getUsername() + "'s&a gamemode to &a"+gameMode.getName()+"&a.");
        } else {
            user.sendMessage("&cThat user is not online.");
        }
    }
}