package com.dank1234.plugin.global.general.events;

import com.dank1234.utils.RankUtils;
import com.dank1234.utils.command.Event;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@Event
public class ChatFormatter implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        String msg = e.getMessage();
        e.setCancelled(true);

        Message.create("&r"+RankUtils.getPrefix(e.getPlayer())+"&r "+e.getPlayer().getName()+" &8» &r"+msg).send(false);
    }
}
