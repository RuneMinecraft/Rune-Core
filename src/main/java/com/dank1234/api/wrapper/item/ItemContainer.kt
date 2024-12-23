package com.dank1234.api.wrapper.item

import com.dank1234.api.wrapper.inventory.Menu
import java.util.*
import java.util.function.Consumer

@Suppress("")
class ItemContainer {
    /*
    private var items: MutableMap<Int, Item>? = null

    private fun ItemContainer(items: MutableMap<Int, Item>) {
        this.items = items
    }

    fun create() {
        return ItemContainer(LinkedHashMap<Int, Item>())
    }

    private fun create(items: Map<Int, Item>): ItemContainer {
        return com.dank1234.api.wrapper.item.ItemContainer(items)
    }

    fun items(): MutableMap<Int, Item>? {
        return this.items
    }

    fun getAllItems(): List<Item> {
        return Arrays.asList<Item>(*items()!!.values.toArray<Item> {  .__Array__() })
    }

    fun add(vararg items: Item): ItemContainer {
        Arrays.stream(items).toList().forEach(Consumer { item: Item ->
            this.set(
                items()!!.size, item
            )
        })
        return this
    }

    fun set(key: Int, value: Item): ItemContainer {
        items()!![key] = value
        return this
    }

    fun toMenu(): Menu {
        val menu: Menu = Menu.create("Test", 54)
        items()!!
            .forEach { (key: Int?, value: Item?) ->
                menu.setItem(key, value)
            }
        return menu
    }

     */
}