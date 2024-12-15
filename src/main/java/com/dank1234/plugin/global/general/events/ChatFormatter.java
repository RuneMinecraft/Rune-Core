package com.dank1234.plugin.global.general.events;

import com.dank1234.utils.Consts;
import com.dank1234.utils.Logger;
import com.dank1234.utils.Utils;
import com.dank1234.utils.command.Event;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

@Event
public class ChatFormatter implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        try {
            Optional<User> optional = User.getUser(e.getPlayer().getUniqueId());
            User user;
            if (optional.isEmpty()) {
                user = User.of(e.getPlayer().getUniqueId(), e.getPlayer().getName());
                optional = Optional.of(user);
            }
            user = optional.get();

            String msg = e.getMessage();
            e.setCancelled(true);

            if (user.getGroups().isEmpty()) {
                assert Consts.MEMBER_GROUP != null;
                user.addGroup(Consts.MEMBER_GROUP);
            }
            Message.broadcast("&r" + user.getGroups().getFirst().getPrefix() + "&r " + user.getUsername() + " &8» &r" + msg).send(false);
            Logger.logRaw(Utils.sColour("&r" + user.getGroups().getFirst().getPrefix() + "&r " + user.getUsername() + " &8» &r") + msg);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
