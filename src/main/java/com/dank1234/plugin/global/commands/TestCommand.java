package com.dank1234.plugin.global.commands;

import com.dank1234.plugin.Main;
import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.command.Server;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.command.CommandSender;

import java.util.Objects;

@Cmd (
        server = Server.GLOBAL,
        names = {"test", "tes"},
        perms = {"test"},
        disabled = true
)
public class TestCommand extends ICommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        Message.create(super.sender(), Objects.requireNonNull(Main.get().config().getValue("name"))).send(true);
    }
}
