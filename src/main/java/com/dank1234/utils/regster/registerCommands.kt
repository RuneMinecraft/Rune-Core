package com.dank1234.utils.regster

import com.dank1234.plugin.Codex
import com.dank1234.plugin.Main
import com.dank1234.utils.Locale
import com.dank1234.utils.Logger
import com.dank1234.utils.command.Command
import com.dank1234.utils.command.ICommand
import com.dank1234.utils.server.ServerType
import com.dank1234.utils.wrapper.player.User

import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player

import org.reflections.Reflections
import org.reflections.scanners.TypeAnnotationsScanner

import java.util.*

fun registerCommands() {
    val reflections = Reflections("com.dank1234.plugin", TypeAnnotationsScanner())
    val annotatedClasses = reflections.getTypesAnnotatedWith(Command::class.java, true)

    if (annotatedClasses.isEmpty()) {
        Logger.logRaw("[Bootstrap | Commands] No commands found.")
        return
    }
    val currentServer = ServerType.HUB

    for (clazz in annotatedClasses) {
        try {
            if (ICommand::class.java.isAssignableFrom(clazz)) {
                val cmd = clazz.getDeclaredConstructor().newInstance() as ICommand

                val commandAnnotation = clazz.getAnnotation(
                    Command::class.java
                )
                if (commandAnnotation != null) {
                    if ((commandAnnotation.server != ServerType.GLOBAL) && currentServer != commandAnnotation.server) {
                        continue
                    }
                    cmd.names(commandAnnotation.names)

                    for (name in commandAnnotation.names) {
                        Codex.addCommand(name, cmd)
                        toBukkit(name, commandAnnotation)
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}

fun execute(
    sender: CommandSender?,
    command: org.bukkit.command.Command,
    args: Array<String>
): Boolean {
    var handler: ICommand? = null
    try {
        if (Codex.getAllCommandNames().contains(command.name.lowercase(java.util.Locale.getDefault()))) {
            handler = Codex.getCommand(command.name.lowercase(java.util.Locale.getDefault()))
                .setSender(sender)
                .args(args)
            val user = handler.sender()

            if (handler.disabled()) {
                user.sendMessage(Locale.DISABLED_COMMAND)
                return false
            }
            handler.execute(user, *handler.args())
        }
    } catch (e: java.lang.Exception) {
        handler?.sender()?.sendMessage(Locale.EXCEPTION_THROWN)
        Logger.log(Locale.EXCEPTION_THROWN)
        e.printStackTrace()
    }
    return true
}

private fun toBukkit(
    name: String,
    commandAnnotation: Command
) {
    try {
        val bukkitCommandMap = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        bukkitCommandMap.isAccessible = true
        val commandMap = bukkitCommandMap[Bukkit.getServer()] as CommandMap

        val command: BukkitCommand = object : BukkitCommand(name) {
            override fun execute(sender: CommandSender, commandLabel: String, args: Array<String>): Boolean {
                if (commandAnnotation.playerOnly && sender !is Player) {
                    User.of(sender.name).sendMessage(Locale.PLAYER_ONLY_COMMAND)
                    return false
                }
                return execute(sender, this, args)
            }
        }
        val aliases: MutableList<String> = ArrayList(Arrays.stream(commandAnnotation.names).toList())
        aliases.remove(name)
        command.setAliases(aliases)

        commandMap.register(Main.get().name, command)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}