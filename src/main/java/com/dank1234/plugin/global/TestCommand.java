package com.dank1234.plugin.global;

import com.dank1234.plugin.Main;
import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import org.bukkit.command.CommandSender;

@Cmd(disabled = false)
public class TestCommand extends ICommand {
    @Override
    public void init() {
        setNames("test");
        setArgs();
        setPermissions();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(Main.get().config().getValue("name"));
    }
}
