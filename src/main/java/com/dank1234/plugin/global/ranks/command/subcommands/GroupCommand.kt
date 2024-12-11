package com.dank1234.plugin.global.ranks.command.subcommands

import com.dank1234.plugin.global.ranks.Group
import com.dank1234.utils.wrapper.message.Message
import org.bukkit.command.CommandSender
import java.io.IOException

class GroupCommand {
    companion object {
        @JvmStatic
        @Throws(IOException::class)
        fun execute(sender: CommandSender, args: Array<String>) {
            if (args.size < 5) {
                Message.create(sender, "&cInvalid group command usage. Type &f'/ranks help'&c for help.").send()
                return
            }

            val groupName = args[1]
            val action = args[2].lowercase()
            val target = args[3].lowercase()
            val value = args[4]

            val group = Group.get(groupName)
            if (group == null) {
                Message.create(sender, "&cGroup &f$groupName&c does not exist!").send()
                return
            }

            when (action) {
                "name" -> {
                    group.name = value
                    Message.create(sender, "&aSet name of group &f$groupName&a to &f$value&a.").send()
                }
                "prefix" -> {
                    group.prefix = value
                    Message.create(sender, "&aSet prefix of group &f$groupName&a to &f$value&a.").send()
                }
                "suffix" -> {
                    group.suffix = value
                    Message.create(sender, "&aSet suffix of group &f$groupName&a to &f$value&a.").send()
                }
                "inherited-groups" -> {
                    when (target) {
                        "add" -> {
                            group.addInheritedGroup(value)
                            Message.create(sender, "&aAdded inherited group &f$value&a to group &f$groupName&a.").send()
                        }
                        "remove" -> {
                            group.removeInheritedGroup(value)
                            Message.create(sender, "&cRemoved inherited group &f$value&c from group &f$groupName&c.").send()
                        }
                        else -> Message.create(sender, "&cUnknown inherited-groups action. Use &fadd&c or &fremove&c.").send()
                    }
                }
                "permissions" -> {
                    when (target) {
                        "add" -> {
                            group.addPermission(value)
                            Message.create(sender, "&aAdded permission &f$value&a to group &f$groupName&a.").send()
                        }
                        "remove" -> {
                            group.removePermission(value)
                            Message.create(sender, "&cRemoved permission &f$value&c from group &f$groupName&c.").send()
                        }
                        else -> Message.create(sender, "&cUnknown permissions action. Use &fadd&c or &fremove&c.").send()
                    }
                }
                else -> Message.create(sender, "&cUnknown group action. Use &fname&c, &fprefix&c, &fsuffix&c, &finherited-groups&c, or &fpermissions&c.").send()
            }
        }
    }
}
