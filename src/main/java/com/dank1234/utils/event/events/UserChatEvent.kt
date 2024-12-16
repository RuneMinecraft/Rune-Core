package com.dank1234.utils.event.events

import com.dank1234.utils.Logger
import com.dank1234.utils.event.RuneEvent
import com.dank1234.utils.wrapper.message.Message
import com.dank1234.utils.wrapper.player.User

class UserChatEvent(
    val user: User,
    var message: String
) : RuneEvent {
    private var format: String = "<${user.username}> $message"

    fun message(message: String) {
        this.message = message
    }
    fun format(format: String) {
        this.format = format
    }

    override fun execute() {
        Message.broadcast(format).send()
        Logger.logRaw(format)
    }
    override fun getName(): String {
        return this.javaClass.simpleName
    }
}