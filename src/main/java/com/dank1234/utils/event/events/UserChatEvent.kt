package com.dank1234.utils.event.events

import com.dank1234.utils.Logger
import com.dank1234.utils.Utils
import com.dank1234.utils.event.RuneEvent
import com.dank1234.utils.wrapper.message.Message
import com.dank1234.utils.wrapper.player.User

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