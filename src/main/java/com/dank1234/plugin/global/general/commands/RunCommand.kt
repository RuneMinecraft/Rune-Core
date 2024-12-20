package com.dank1234.plugin.global.general.commands

import com.dank1234.api.Locale
import com.dank1234.api.command.Command
import com.dank1234.api.command.ICommand
import com.dank1234.api.wrapper.player.User

@Command(names=["run", "cmd"])
class RunCommand : ICommand() {
    override fun execute(user: User, vararg args: String) { // run (cmd) 100
        // TODO: Permission Check! (rune.admin.run)

        if (args.size <= 1) {
            user.sendMessage(Locale.INVALID_ARGUMENTS)
            return
        }
        val countArg = args.lastOrNull()
        val repeatCount = countArg?.toIntOrNull()

        if (repeatCount == null || repeatCount <= 0) {
            user.sendMessage(Locale.INVALID_NUMBER_FORMAT+"&c The number needs to be atleast 1!")
            return
        }
        for (i in 1..repeatCount) {
            user.sudo(args.dropLast(1).joinToString(" "))
        }
    }

}