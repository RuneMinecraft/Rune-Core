package com.dank1234.plugin.global.general.events

import com.dank1234.utils.event.Event
import com.dank1234.utils.event.RuneListener
import com.dank1234.utils.event.events.UserQuitEvent

class LeaveHandler : RuneListener() {
    @Event fun onQuit(e: UserQuitEvent) {
        e.leaveMessage("&8[&c&l-&8] &7${e.user.username}")
    }
}