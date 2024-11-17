package com.dank1234.plugin.global.general.events;

import com.dank1234.utils.command.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

@Event
public class ResourcePackListener implements Listener {
    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        PlayerResourcePackStatusEvent.Status status = event.getStatus();

        if (status != PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            player.kick();
        }
    }
}