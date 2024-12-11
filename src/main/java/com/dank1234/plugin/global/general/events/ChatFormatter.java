package com.dank1234.plugin.global.general.events;

import com.dank1234.utils.Consts;
import com.dank1234.utils.command.Event;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@Event
public class ChatFormatter implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        try {
            User user = User.of(e.getPlayer().getUniqueId());
            String msg = e.getMessage();
            e.setCancelled(true);

            assert user != null;
            if (user.getGroups().isEmpty()) {
                user.addGroup(Consts.MEMBER_GROUP);
            }
            Message.broadcast("&r" + user.getGroups().getFirst().getPrefix() + "&r " + user.getUsername() + " &8» &r" + msg).send(false);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
