package com.dank1234.utils.event;

import com.dank1234.utils.event.events.UserChatEvent;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class EventBridge implements Listener {
    private final RuneEventManager runeEventManager;

    public EventBridge(RuneEventManager runeEventManager) {
        this.runeEventManager = runeEventManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        User user = User.of(e.getPlayer().getUniqueId());
        String message = e.getMessage();

        UserChatEvent customEvent = new UserChatEvent(user, message);
        runeEventManager.triggerEvent(customEvent);
    }
}
