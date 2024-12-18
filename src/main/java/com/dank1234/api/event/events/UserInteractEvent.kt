package com.dank1234.api.event.events

import com.dank1234.api.event.RuneEvent
import com.dank1234.api.wrapper.item.Item
import com.dank1234.api.wrapper.player.User

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