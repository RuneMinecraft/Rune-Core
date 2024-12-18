package com.dank1234.plugin.global.ranks.command;

import com.dank1234.api.command.Command;
import com.dank1234.api.command.ICommand;

import com.dank1234.api.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

@Command(names = "track")
public class TrackCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        if (args.length == 0) {
            return;
        }

    }
}
