package com.dank1234.plugin.global.general.events

import com.dank1234.api.event.Event
import com.dank1234.api.event.RuneListener
import com.dank1234.api.event.events.UserQuitEvent

class LeaveHandler : RuneListener() {
    @Event fun onQuit(e: UserQuitEvent) {
        e.leaveMessage("&8[&c&l-&8] &7${e.user.username}")
    }
}