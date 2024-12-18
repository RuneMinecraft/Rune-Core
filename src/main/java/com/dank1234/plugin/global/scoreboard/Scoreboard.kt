package com.dank1234.plugin.global.scoreboard

import com.dank1234.api.wrapper.player.User

class Scoreboard(
    val user: User,
    private var title: String = "Scoreboard",
    private val lines: MutableList<String> = mutableListOf()
) {
    fun setTitle(newTitle: String) {
        title = newTitle
    }

    fun addLine(line: String): Scoreboard {
        lines.add(line)
        return this
    }
    fun removeLine(line: String): Boolean {
        return lines.remove(line)
    }
    fun getLine(num: Int): String = lines[num]
    fun clearLines() {
        lines.clear()
    }
    fun getLines(): List<String> {
        return lines.toList()
    }

    fun display() {

    }
}