package com.dank1234.api.event.events

import com.dank1234.api.Logger
import com.dank1234.api.Utils
import com.dank1234.api.event.RuneEvent
import com.dank1234.api.wrapper.message.Message
import com.dank1234.api.wrapper.player.User

class UserJoinEvent(
    val user: User
) : RuneEvent() {
    private var joinMessage: String = "&f${user.username} &ejoined the game."
    fun joinMessage(joinMessage: String) {
        this.joinMessage = joinMessage
    }

    override fun execute() {
        Message.broadcast(joinMessage).send()
        Logger.log(Utils.sColour(joinMessage))
    }

    override fun getName(): String {
        return this.javaClass.simpleName
    }
}