package com.dank1234.utils.event.events

import com.dank1234.utils.Logger
import com.dank1234.utils.Utils
import com.dank1234.utils.event.RuneEvent
import com.dank1234.utils.wrapper.message.Message
import com.dank1234.utils.wrapper.player.User

class UserQuitEvent(
    val user: User
) : RuneEvent() {
    private var leaveMessage: String = "&f${user.username} &eleft the game."
    fun leaveMessage(leaveMessage: String) {
        this.leaveMessage = leaveMessage
    }

    override fun execute() {
        Message.broadcast(leaveMessage).send()
        Logger.log(Utils.sColour(leaveMessage))
    }

    override fun getName(): String {
        return this.javaClass.simpleName
    }
}