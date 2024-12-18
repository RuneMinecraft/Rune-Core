package com.dank1234.plugin.global.general.commands;

import com.dank1234.api.command.Command;
import com.dank1234.api.command.ICommand;
import com.dank1234.api.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

@Command(names="fly")
public class FlyCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        // TODO: Permission Check! (rune.player.fly)
        user.sendMessage(super.sender().getPlayer().getAllowFlight() ? "&cDisabled fly." : "&aEnabled fly.");
        super.sender().getPlayer().setAllowFlight(!super.sender().getPlayer().getAllowFlight());
    }
}
