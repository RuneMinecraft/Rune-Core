package com.dank1234.utils.event.events

import com.dank1234.utils.event.RuneEvent
import com.dank1234.utils.wrapper.item.Item
import com.dank1234.utils.wrapper.player.User

class UserInteractEvent(
    val user: User,
    val item: Item,
    private var clickType: ClickType
) : RuneEvent() {
    fun clickType(type: ClickType) {
        this.clickType = type
    }

    override fun execute() {
        println("Event[$name] User[${user.username}] Item[${item.displayName()}] ClickType[${clickType.name}]")
    }

    enum class ClickType {
        RIGHT_CLICK_AIR,
        RIGHT_CLICK_BLOCK,
        LEFT_CLICK_AIR,
        LEFT_CLICK_BLOCK,
        EMPTY;
    }
}