package com.dank1234.plugin.global.chat;

import com.dank1234.utils.Locale;
import com.dank1234.utils.Result;
import com.dank1234.utils.command.Command;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

@Command(names = {"chat-colour", "chatcolour", "colour", "chat-color", "chatcolor", "color"})
public class ChatColourCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, @NotNull String ... args) {
        Result result = user.setChatColour(args(0));
        switch (result) {
            case SUCCESSFUL -> user.sendMessage("&aSet your chat color to: &f"+args(0)+"&l"+Colour.getByCode(args(0)).name());
            case FAILURE -> user.sendMessage("&cIncorrect format! Use &{color}. You can also use /color list!");
            case EXCEPTION -> user.sendMessage(Locale.EXCEPTION_THROWN);
        }
    }
}
