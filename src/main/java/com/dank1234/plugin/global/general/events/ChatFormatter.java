package com.dank1234.plugin.global.general.events;

import com.dank1234.utils.RankUtils;
import com.dank1234.utils.command.Event;
import com.dank1234.utils.wrapper.message.Message;
import com.dank1234.utils.wrapper.player.User;
import com.dank1234.utils.wrapper.player.staff.Staff;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@Event
public class ChatFormatter implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        String msg = e.getMessage();
        e.setCancelled(true);

        String prefix;

        Staff staff = Staff.of(e.getPlayer().getUniqueId());
        if (staff == null) {
            prefix = "";
        }else {
            prefix = staff.rank().toString();
        }

        Message.create(prefix+" &r"+RankUtils.getPrefix(User.of(e.getPlayer().getUniqueId()))+"&r "+e.getPlayer().getName()+" &8» &r"+msg).send(false);
    }
}
