package com.dank1234.plugin.global.general.commands;

import com.dank1234.api.Locale;
import com.dank1234.api.Result;
import com.dank1234.api.command.Command;
import com.dank1234.api.command.ICommand;
import com.dank1234.api.wrapper.player.GameMode;
import com.dank1234.api.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@Command(
        names = {"gmc", "gmcreative", "creative"},
        playerOnly = true
)
public class CreativeCommand extends ICommand {
    private final GameMode gameMode = GameMode.CREATIVE;

    @Override
    public void execute(@NotNull User user, String[] args) {
        // TODO: Permission Check! (rune.gamemode.creative)
        if (args.length == 0) {
            Result result = user.setGameMode(this.gameMode);
            switch (result) {
                case SUCCESSFUL -> user.sendMessage("&aSet your gamemode to &f"+gameMode.getName()+"&a.");
                case EXCEPTION -> user.sendMessage(Locale.EXCEPTION_THROWN);
            }
            return;
        }

        Optional<User> optionalUser = User.getUser(args(0));
        if (optionalUser.isEmpty()) {
            user.sendMessage("&cThe player &f"+args(0)+"&c does not exist in our database.");
            return;
        }

        User target = optionalUser.get();
        if (target.isOnline()) {
            Result result = target.setGameMode(this.gameMode);
            switch (result) {
                case SUCCESSFUL -> user.sendMessage("&aSet &f" + target.getUsername() + "&a's gamemode to &a"+gameMode.getName()+"&a.");
                case EXCEPTION -> user.sendMessage(Locale.EXCEPTION_THROWN);
            }
        } else {
            user.sendMessage("&cThat user is not online.");
        }
    }
}