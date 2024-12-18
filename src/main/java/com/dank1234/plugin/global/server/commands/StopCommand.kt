package com.dank1234.plugin.global.server.commands

import com.dank1234.api.command.Command
import com.dank1234.api.command.ICommand
import com.dank1234.api.wrapper.message.Message
import com.dank1234.api.wrapper.player.User
import org.bukkit.Bukkit
import java.time.Duration

@Command(names = ["stop", "shutdown"])
class StopCommand : ICommand() {
    override fun execute(user: User, vararg args: String) {
        Thread {
            var i = 5
            while (i != 0) {
                Message.broadcast("&cShutting down in &f&l$i&c seconds!").send()
                Thread.sleep(Duration.ofSeconds(1))
                i--
            }
            Bukkit.getServer().shutdown()
        }.start()
    }
}