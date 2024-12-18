package com.dank1234.plugin.global.general.commands

import com.dank1234.api.Locale
import com.dank1234.api.Result
import com.dank1234.api.command.Command
import com.dank1234.api.command.ICommand
import com.dank1234.api.wrapper.player.User

@Command(names = ["nick", "nickname"])
class NickCommand : ICommand() {
    override fun execute(user: User, vararg args: String) {
        // TODO: Permission Checker! (rune.player.nick)

        if (args.size != 1) {
            user.sendMessage(Locale.INVALID_ARGUMENTS + "/nick <name>")
            return
        }

        val result = user.setNickname(args(0))
        when (result) {
            Result.SUCCESSFUL -> user.sendMessage("&aSet your nickname to: &f${user.getNickname()}")
            Result.FAILURE -> user.sendMessage("&cYour nick must only contain [A-Z | 0-9 | _].")
            Result.BOUND_FAILURE -> user.sendMessage("&cYour nick must range from 3-16 characters!")
            Result.EXCEPTION -> user.sendMessage(Locale.EXCEPTION_THROWN)
        }
    }
}