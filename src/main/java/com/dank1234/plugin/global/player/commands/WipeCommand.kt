package com.dank1234.plugin.global.player.commands

import com.dank1234.plugin.Codex
import com.dank1234.utils.command.Command
import com.dank1234.utils.command.ICommand
import com.dank1234.utils.data.Database
import com.dank1234.utils.wrapper.player.User
import net.kyori.adventure.text.Component

@Command(names=["wipe-data"])
class WipeCommand : ICommand() {
    override fun execute(user: User, vararg args: String) {
        var target = user
        if (args.size == 1) {
            target = User.of(args(0))
        }
        user.sendMessage("&cDeleting data for user &f${target.username}&c...")
        target.getPlayer().kick(Component.text(Colour("&c&lI JUST WIPED YOUR MFING DATA IDIOT!!!")))
        Codex.removeUser(target.uuid)
        Database.SQLUtils.executeUpdate("DELETE FROM users WHERE uuid = ?") { stmt -> stmt.setString(1, target.uuid.toString())}
    }
}