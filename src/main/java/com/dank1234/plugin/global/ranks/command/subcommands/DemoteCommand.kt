package com.dank1234.plugin.global.ranks.command.subcommands

import com.dank1234.plugin.global.ranks.Track
import com.dank1234.api.wrapper.message.Message
import com.dank1234.api.wrapper.player.User
import org.bukkit.command.CommandSender
import java.io.IOException

class DemoteCommand {
    companion object {
        @JvmStatic
        @Throws(IOException::class)
        fun execute(sender: CommandSender, args: Array<String>) {
            if (args.size < 3) {
                Message.create(sender, "&cInvalid demote command usage. Use &f'/ranks demote [USER] [TRACK]'&c.").send()
                return
            }

            val username = args[1]
            val trackName = args[2]

            val user = User.of(username);

            val track = Track.get(trackName)
            if (track == null) {
                Message.create(sender, "&cTrack &f$trackName&c does not exist!").send()
                return
            }

            val currentLevel = user.tracks[track] ?: 0
            if (currentLevel <= 0) {
                Message.create(sender, "&cUser &f$username&c is already at the lowest level in track &f$trackName&c.").send()
                return
            }

            user.tracks[track] = currentLevel - 1
            Message.create(sender, "&aDemoted user &f$username&a in track &f$trackName&a to level &f${currentLevel - 1}&a.").send()
        }
    }
}
