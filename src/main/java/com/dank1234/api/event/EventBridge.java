package com.dank1234.api.event;

import com.dank1234.api.event.events.UserChatEvent;
import com.dank1234.api.event.events.UserInteractEvent;
import com.dank1234.api.event.events.UserJoinEvent;
import com.dank1234.api.event.events.UserQuitEvent;
import com.dank1234.api.wrapper.item.Item;
import com.dank1234.api.wrapper.player.User;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        e.joinMessage(Component.empty());
        User user = User.of(e.getPlayer().getUniqueId(), e.getPlayer().getName());

        UserJoinEvent customEvent = new UserJoinEvent(user);
        runeEventManager.triggerEvent(customEvent);
    }
    public @EventHandler void onPlayerQuit(PlayerQuitEvent e) {
        e.quitMessage(Component.empty());
        User user = User.of(e.getPlayer().getUniqueId(), e.getPlayer().getName());

        UserQuitEvent customEvent = new UserQuitEvent(user);
        runeEventManager.triggerEvent(customEvent);
    }
    public @EventHandler void onPlayerInteract(PlayerInteractEvent e) {
        User user = User.of(e.getPlayer().getUniqueId(), e.getPlayer().getName());

        Action action = e.getAction();
        UserInteractEvent.ClickType clickType = switch (action) {
            case LEFT_CLICK_AIR -> UserInteractEvent.ClickType.LEFT_CLICK_AIR;
            case LEFT_CLICK_BLOCK -> UserInteractEvent.ClickType.LEFT_CLICK_BLOCK;
            case RIGHT_CLICK_AIR -> UserInteractEvent.ClickType.RIGHT_CLICK_AIR;
            case RIGHT_CLICK_BLOCK -> UserInteractEvent.ClickType.RIGHT_CLICK_BLOCK;
            case PHYSICAL -> UserInteractEvent.ClickType.EMPTY;
        };

        UserInteractEvent customEvent = new UserInteractEvent(
                user, user.getHeldItem() == null
                    ? Item.create(Material.AIR)
                    : user.getHeldItem(),
                clickType
        );
        runeEventManager.triggerEvent(customEvent);
    }
}
