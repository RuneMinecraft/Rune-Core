package com.dank1234.plugin.global.general.events

import com.dank1234.utils.event.Event
import com.dank1234.utils.event.RuneListener
import com.dank1234.utils.event.events.UserJoinEvent

class JoinHandler : RuneListener() {
    @Event fun onPlayerJoin(e: UserJoinEvent) {
        if (!e.user.hasJoinedBefore) {
            e.user.sendMessage("""
&#a366ff&m&l-------------&r Welcome to ✴ &#be00fd&lRune&#5ee7ff&lMC&r ✴ &#a366ff&m&l-------------&r
                   &7&oYou have joined &l&oArcane Box&r
                
                &#45e658&lSTORE&r    https://store.runemc.net/&r
                &#7289da&lDISCORD&r  https://discord.runemc.net/&r
                &#e6b345&lRULES&r    https://rules.runemc.net/&r
                 
&#a366ff&m&l---------------------------------------------&r
            """.trimIndent())
        }
        e.joinMessage("&8[&a&l+&8] &7${e.user.username}")
    }
}