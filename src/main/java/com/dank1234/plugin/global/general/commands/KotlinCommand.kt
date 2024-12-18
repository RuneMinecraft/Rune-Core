package com.dank1234.plugin.global.general.commands

import com.dank1234.api.command.Command
import com.dank1234.api.command.ICommand
import com.dank1234.api.wrapper.player.User

@Command(names=["kotlin"])
class KotlinCommand : ICommand() {
    override fun execute(user: User, vararg args: String) {
        TODO("Not yet implemented")
    }
}