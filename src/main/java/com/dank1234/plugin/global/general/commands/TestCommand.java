package com.dank1234.plugin.global.general.commands;

import com.dank1234.plugin.global.ranks.Group;
import com.dank1234.utils.command.Cmd;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.command.CommandSender;

import java.io.IOException;

@Cmd(names = "test")
public class TestCommand extends ICommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        User user = User.of(sender.getName());
        try {
            Group.Companion.create("member", "&7&lMember", null, 1);
            user.addGroup(Group.Companion.get("member"));
            Message.create(user, user.getGroups().toString()).send();
        }catch(Exception ignored) {
        }
    }
}