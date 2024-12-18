package com.dank1234.plugin.global.ranks.command;

import com.dank1234.plugin.global.ranks.Group;
import com.dank1234.plugin.global.ranks.Ranks;
import com.dank1234.plugin.global.ranks.Track;
import com.dank1234.utils.command.Command;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Command(names = "user")
public class UserCommand extends ICommand {
    @Override
    public void execute(@NotNull User user, String[] args) {
        if (args.length == 0) {
            return;
        }
        User target = User.of(args(0));

        List<String> groups = target.getGroups().stream()
                .map(Group::getName)
                .toList();

        List<String> tracks = target.getTracks().keySet().stream()
                .map(Track::getName)
                .toList();

        List<String> perms = new ArrayList<>(target.getPermissions());

        Message.create(user,
                "&aInfo for &f"+target.getUsername()+"&a:",
                "&a| &3User: &f"+target.getUsername(),
                "&a| &3UUID: &f"+target.getUuid(),
                " ",
                "&a| &3Tokens: &f"+target.tokens(),
                "&a| &3Souls: &f"+target.souls(),
                "&a| &3Gems: &f"+target.gems(),
                " ",
                "&a| &3&lGroups &8»",
                formatList(groups),
                " ",
                "&a| &3&lTracks &8»",
                formatList(tracks),
                " ",
                "&a| &3&lPermissions &8»",
                formatList(perms)
        ).send();
    }

    private String formatList(List<String> list) {
        if (list.isEmpty()) {
            return "&8- &fNone";
        }
        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append("&8- &f").append(item).append("\n");
        }
        return sb.toString();
    }
}
