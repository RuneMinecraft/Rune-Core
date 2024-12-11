package com.dank1234.plugin.global.general.events;

import com.dank1234.utils.command.Event;
import com.dank1234.utils.data.database.EcoManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Event
public class JoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        User.of(e.getPlayer().getUniqueId(), e.getPlayer().getName());
        EcoManager.insert(User.of(e.getPlayer().getUniqueId()));
    }
}
