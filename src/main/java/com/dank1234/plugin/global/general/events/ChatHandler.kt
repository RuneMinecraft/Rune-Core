package com.dank1234.plugin.global.general.events

import com.dank1234.plugin.global.ranks.Ranks
import com.dank1234.utils.Consts
import com.dank1234.utils.event.Event
import com.dank1234.utils.event.RuneListener
import com.dank1234.utils.event.events.UserChatEvent
import com.dank1234.utils.wrapper.player.GameMode

class ChatHandler : RuneListener() {
    @Event fun onChat(e: UserChatEvent) {
        if (e.user.groups.isEmpty()) {
            e.user.addGroup(Ranks.MEMBER.group())
        }
        if (e.user.username == "AGCol_" && e.user.gamemode() == GameMode.ADVENTURE) {
            e.message("oh shit, turns out i'm a massive twink, or in other words, i'm a sado g")
        }
        e.format("&r${e.user.groups.last().prefix} &r${e.user.username} &8» &r${e.user.getChatColour()}${e.message}")
    }
}

