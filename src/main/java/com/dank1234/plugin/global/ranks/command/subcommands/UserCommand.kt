package com.dank1234.plugin.global.ranks.command.subcommands

import com.dank1234.plugin.global.ranks.Group
import com.dank1234.plugin.global.ranks.Track
import com.dank1234.utils.wrapper.message.Message
import com.dank1234.utils.wrapper.player.User
import org.bukkit.command.CommandSender
import java.io.IOException

class UserCommand {
    companion object {
        @JvmStatic
        fun execute(sender: CommandSender, args: Array<String>) {
            try {
                if (args.size < 5) {
                    Message.create(sender, "&cInvalid arguments! Type '/ranks help' for help.").send()
                    return
                }

                val username = args[1]
                val action = args[2].lowercase()
                val target = args[3].lowercase()
                val value = args[4]

                val user = User.of(username)

                when (action) {
                    "group" -> {
                        when (target) {
                            "set" -> {
                                user.clearGroups()
                                Group.get(value)?.let { user.addGroup(it) }
                                if (!Group.exists(value)!!) {
                                    user.sendMessage("&cCannot give &f$value &cto &f$username &cas it does not exist.")
                                    return
                                }
                                Message.create(sender, "&aSet &f$username's&a primary group to &f$value&a.").send()
                            }

                            "add" -> {
                                Group.get(value)?.let { user.addGroup(it) }
                                Message.create(sender, "&aAdded &f$value&a to &f$username's&a groups.").send()
                            }

                            "remove" -> {
                                Group.get(value)?.let { user.removeGroup(it) }
                                Message.create(sender, "&cRemoved &f$value&f from &f$username's&c groups.").send()
                            }

                            else -> Message.create(sender, "&cInvalid group action. Use set/add/remove.").send()
                        }
                    }

                    "track" -> {
                        when (target) {
                            "add" -> {
                                Track.get(value)?.let { user.addTrack(it, 0) }
                                Message.create(sender, "&aSet &f$username's&a track to &a$value").send()
                            }

                            "remove" -> {
                                Track.get(value)?.let { user.removeTrack(it) }
                                Message.create(sender, "&cRemoved &f$value&f from &f$username's&c tracks.").send()
                            }

                            else -> Message.create(sender, "&cInvalid track action. Use set/remove.").send()
                        }
                    }

                    "permissions" -> {
                        when (target) {
                            "add" -> {
                                user.addPermission(value)
                                Message.create(sender, "&aAdded permission &f$value&a to user &f$username").send()
                            }

                            "remove" -> {
                                user.removePermission(value)
                                Message.create(sender, "&cRemoved permission &f$value&c from user &f$username").send()
                            }

                            else -> Message.create(sender, "&cUnknown permission action. Use add/remove.").send()
                        }
                    }

                    else -> Message.create(sender, "&cUnknown user action. Use group/track/permissions.").send()
                }
            }catch (e: Exception) {}
        }
    }
}