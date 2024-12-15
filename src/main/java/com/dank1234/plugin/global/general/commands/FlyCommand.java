package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Command;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

@Command(names="fly")
public class FlyCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        user.sendMessage(super.sender().getPlayer().getAllowFlight() ? "&cDisabled fly." : "&aEnabled fly.");
        super.sender().getPlayer().setAllowFlight(!super.sender().getPlayer().getAllowFlight());
    }
}
