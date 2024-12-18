package com.dank1234.plugin.global.spawn

import com.dank1234.api.command.Command
import com.dank1234.api.command.ICommand
import com.dank1234.api.wrapper.player.User

@Command(names = ["spawn"], playerOnly = true)
class SpawnCommand : ICommand() {
    override fun execute(user: User, vararg args: String) {
        user.sendMessage("&eTeleporting...")
        user.teleport(Spawn.get())
    }
}