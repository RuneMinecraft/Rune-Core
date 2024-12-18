package com.dank1234.plugin.global.general.events

import com.dank1234.plugin.global.ranks.Ranks
import com.dank1234.api.event.Event
import com.dank1234.api.event.RuneListener
import com.dank1234.api.event.events.UserChatEvent
import com.dank1234.api.wrapper.player.GameMode

class ChatHandler : RuneListener() {
    @Event fun onChat(e: UserChatEvent) {
        if (e.user.groups.isEmpty()) {
            e.user.addGroup(Ranks.MEMBER.group())
        }
        if (e.user.username == "AGCol_" && e.user.gamemode() == GameMode.ADVENTURE) {
            e.message("oh shit, turns out i'm a massive twink, or in other words, i'm a sado g")
        };
        val name: String = if (e.user.getNickname() == "") {
            e.user.username
        }else {
            e.user.getNickname()
        }

        e.format("&r${e.user.groups.last().prefix} &r${name} &8» &r${e.user.getChatColour()}${e.message}")
    }
}

