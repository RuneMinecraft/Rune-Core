package com.dank1234.plugin.global.general.events;

import com.dank1234.utils.event.Event;
import com.dank1234.utils.event.RuneListener;
import com.dank1234.utils.event.events.UserJoinEvent;

public class JoinListener extends RuneListener {
    @Event()
    public void onPlayerJoin(UserJoinEvent e) {
        e.format("&8[&a&l+&8] &7"+e.getUser().getUsername());
    }
}
