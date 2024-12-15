package com.dank1234.plugin.global.ranks.command;

import com.dank1234.utils.command.Command;
import com.dank1234.utils.command.ICommand;

import com.dank1234.utils.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

@Command(names = "group")
public class GroupCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        if (args.length == 0) {
            return;
        }

    }
}
