package com.dank1234.utils.event.events

import com.dank1234.utils.Logger
import com.dank1234.utils.Utils
import com.dank1234.utils.event.RuneEvent
import com.dank1234.utils.wrapper.message.Message
import com.dank1234.utils.wrapper.player.User

class UserJoinEvent(
    val user: User
) : RuneEvent {
    private var format: String = "&f${user.username} &ejoined the game."
    fun format(format: String) {
        this.format = format
    }

    override fun execute() {
        Message.broadcast(format).send()
        Logger.logRaw(Utils.sColour(format))
    }

    override fun getName(): String {
        return this.javaClass.simpleName
    }
}