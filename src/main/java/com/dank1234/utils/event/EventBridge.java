package com.dank1234.utils.event;

import com.dank1234.utils.event.events.UserChatEvent;
import com.dank1234.utils.event.events.UserJoinEvent;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventBridge implements Listener {
    private final RuneEventManager runeEventManager;

    public EventBridge(RuneEventManager runeEventManager) {
        this.runeEventManager = runeEventManager;
    }

    public @EventHandler void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        User user = User.of(e.getPlayer().getUniqueId());
        String message = e.getMessage();

        UserChatEvent customEvent = new UserChatEvent(user, message);
        runeEventManager.triggerEvent(customEvent);
    }
    public @EventHandler void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage("");
        User user = User.of(e.getPlayer().getUniqueId(), e.getPlayer().getName());

        UserJoinEvent customEvent = new UserJoinEvent(user);
        runeEventManager.triggerEvent(customEvent);
    }
}
