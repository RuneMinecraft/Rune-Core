package com.dank1234.plugin.script

import com.dank1234.api.command.Command
import com.dank1234.api.command.ICommand
import com.dank1234.api.wrapper.player.User
import com.dank1234.plugin.script.actions.load
import com.dank1234.plugin.script.actions.unload
import kotlinx.coroutines.*

@Command(names=["script"])
class ScriptCommand : ICommand() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun execute(user: User, vararg args: String){
        if (args.isEmpty()) {
            user.sendMessage("&cInvalid Arguments! Usage: /script <load|unload|reload|execute> <scriptPath>")
            return
        }

        val subCommand = args[0]
        val scriptName = args.getOrNull(1)

        when (subCommand) {
            "load" -> scriptName?.let {
                val start = System.currentTimeMillis()
                user.sendMessage("&eLoading &f$scriptName...")

                load(it)

                user.sendMessage("&aLoaded &f$scriptName! &7&o(Took ${System.currentTimeMillis()-start}ms)")
            } ?: user.sendMessage("&cPlease specify a script!")

            "unload" -> scriptName?.let {
                val start = System.currentTimeMillis()
                user.sendMessage("&eUnloading &f$scriptName...")

                unload(it)

                user.sendMessage("&aUnloaded &f$scriptName! &7&o(Took ${System.currentTimeMillis()-start}ms)")
            } ?: user.sendMessage("&cPlease specify a script!")

            "reload" -> scriptName?.let {
                val start = System.currentTimeMillis()
                user.sendMessage("&eReloading &f$scriptName...")

                unload(it)
                load(it)

                user.sendMessage("&aReloaded &f$scriptName! &7&o(Took ${System.currentTimeMillis()-start}ms)")
            } ?: user.sendMessage("&cPlease specify a script!")

            "execute" -> scriptName?.let {
                val start = System.currentTimeMillis()
                GlobalScope.launch {
                    try {
                        com.dank1234.plugin.script.actions.execute(scriptName)

                        withContext(Dispatchers.IO) {
                            user.sendMessage("&aExecuted the script: &f$scriptName &7&o(Took ${System.currentTimeMillis()-start}ms)")
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            user.sendMessage("An error occurred: ${e.message}")
                        }
                    }
                }
            } ?: user.sendMessage("&cPlease specify a script!")
            else -> user.sendMessage("&cUnknown subcommand: $subCommand!")
        }
        return
    }
}