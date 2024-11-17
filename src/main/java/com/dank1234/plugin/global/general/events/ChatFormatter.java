package com.dank1234.plugin.global.general.events;

import com.dank1234.plugin.Bootstrap;
import com.dank1234.plugin.Main;
import com.dank1234.utils.RankUtils;
import com.dank1234.utils.command.Event;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

@Event
public class ChatFormatter implements Listener {
    Map<String, String> prefixes = new HashMap<>();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        String msg = e.getMessage();
        e.setCancelled(true);

        if (prefixes.isEmpty()) {
            prefixes.put("&#ff63c8&lᴍᴀɴᴀɢᴇʀ", "ϑЂ");
            prefixes.put("&#ffb300&lᴅᴇᴠ", "ϚЃ");
            prefixes.put("&#e32d2d&lᴀᴅᴍɪɴ", "ϡЄ");
            prefixes.put("&#903dfc&lsʀ ᴍᴏᴅ", "ϨЉ");
            prefixes.put("&#1ec5fc&lᴍᴏᴅ", "ϪЍ");
            prefixes.put("&#02f543&lʜᴇʟᴘᴇʀ", "ϱЏ");
            prefixes.put("&#fcd33f&lʙᴜɪʟᴅᴇʀ", "ϯД");
            prefixes.put("&#ff0000&lʏᴏᴜ&f&lᴛᴜʙᴇ", "ϰЖ");
        }
        String prefix = RankUtils.getPrefix(e.getPlayer());
        prefix = prefixes.get(prefix);

        Message.create("&r"+prefix+"&r "+e.getPlayer().getName()+" &8» &r"+msg).send(false);
    }
}
