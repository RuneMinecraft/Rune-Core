package com.dank1234.plugin.global.spawn;

import com.dank1234.utils.command.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

@Event
public class SpawnListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().teleport(Spawn.get().location());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.getPlayer().teleport(Spawn.get().location());
    }
}
