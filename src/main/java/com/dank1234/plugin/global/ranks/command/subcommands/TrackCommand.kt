package com.dank1234.plugin.global.ranks.command.subcommands

import com.dank1234.plugin.global.ranks.Group
import com.dank1234.plugin.global.ranks.Track
import com.dank1234.utils.wrapper.message.Message
import org.bukkit.command.CommandSender
import java.io.IOException

class TrackCommand {
    companion object {
        @JvmStatic
        @Throws(IOException::class)
        fun execute(sender: CommandSender, args: Array<String>) {
            if (args.size < 5) {
                Message.create(sender, "&cInvalid arguments! Type '/ranks help' for help.").send()
                return
            }

            val trackName = args[1]
            val action = args[2].lowercase()
            val target = args[3].lowercase()
            val value = args[4]

            val track = Track.get(trackName)

            if (action == "group") {
                when (target) {
                    "add" -> {
                        Group.get(value)?.let { track?.addGroup(it.name) }
                        Message.create(sender, "&aAdded group &f$value&a to track &f$trackName&a.").send()
                    }
                    "remove" -> {
                        Group.get(value)?.let { track?.removeGroup(it.name) }
                        Message.create(sender, "&cRemoved group &f$value&c from track &f$trackName&c.").send()
                    }
                    else -> Message.create(sender, "&cUnknown group action. Use&f add&c or&f remove&c.").send()
                }
            } else {
                Message.create(sender, "&cUnknown track action. Use&f group&c.").send()
            }
        }
    }
}
