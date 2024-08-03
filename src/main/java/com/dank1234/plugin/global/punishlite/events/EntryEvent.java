package com.dank1234.plugin.global.punishlite.events;

import com.dank1234.plugin.global.punishlite.players.User;
import com.dank1234.utils.command.Event;
import com.dank1234.utils.data.punishments.UserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@Event
public class EntryEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (User.of(e.getPlayer().getUniqueId()) != null) {
            return;
        }

        UserManager.insert(User.of(e.getPlayer().getUniqueId(), e.getPlayer().getName()));
    }
}
