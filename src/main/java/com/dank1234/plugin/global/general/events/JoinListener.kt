package com.dank1234.plugin.global.general.events;

import com.dank1234.utils.event.Event;
import com.dank1234.utils.event.RuneListener;
import com.dank1234.utils.event.events.UserJoinEvent

class JoinListener : RuneListener() {
    @Event fun onPlayerJoin(e: UserJoinEvent) {
        e.joinMessage("&8[&a&l+&8] &7${e.user.username}")
    }
}