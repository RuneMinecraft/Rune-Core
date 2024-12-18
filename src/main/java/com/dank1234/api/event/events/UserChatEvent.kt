package com.dank1234.api.event.events

import com.dank1234.api.Logger
import com.dank1234.api.Utils
import com.dank1234.api.event.RuneEvent
import com.dank1234.api.wrapper.message.Message
import com.dank1234.api.wrapper.player.User

class UserChatEvent(
    val user: User,
    var message: String
) : RuneEvent() {
    private var format = "<${user.username}> $message"
    private var cancelled = false;

    fun message(message: String) {
        this.message = message
    }
    fun format(format: String) {
        this.format = format
    }
    fun cancelled(cancelled: Boolean) {
        this.cancelled = cancelled
    }

    override fun execute() {
        if (cancelled) {
            return
        }
        Message.broadcast(format).send()
        Logger.log(Utils.sColour(format))
    }
    override fun getName(): String {
        return this.javaClass.simpleName
    }
}