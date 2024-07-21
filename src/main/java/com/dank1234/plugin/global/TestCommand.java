package com.dank1234.plugin.global;

import com.dank1234.plugin.Main;
import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.command.Server;
import org.bukkit.command.CommandSender;

import java.util.Objects;

@Cmd(
        server = Server.GLOBAL,
        names = {"test"},
        perms = {"test"},
        disabled = false
)
public class TestCommand extends ICommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(Objects.requireNonNull(Main.get().config().getValue("name")));
    }
}
