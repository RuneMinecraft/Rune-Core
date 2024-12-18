package com.dank1234.plugin.global.development.commands

import com.dank1234.api.command.Command
import com.dank1234.api.command.ICommand
import com.dank1234.api.wrapper.player.User

@Command(names=["dev/test"])
class TestCommand : ICommand(){
    override fun execute(user: User, vararg args: String) {
        // TODO: Permission Check! (rune.dev.test)

        user.sendMessage("&cNo Current Test!")
    }
}