package com.dank1234.plugin.global.server.commands

import com.dank1234.utils.command.Command
import com.dank1234.utils.command.ICommand
import com.dank1234.utils.wrapper.message.Message
import com.dank1234.utils.wrapper.player.User
import org.bukkit.Bukkit
import java.time.Duration

@Command(names = ["stop", "shutdown"])
class StopCommand : ICommand() {
    override fun execute(user: User, vararg args: String) {
        Thread {
            var i = 5
            while (i != 0) {
                Message.broadcast("&c&lShutting down in &f&l&n$i &c&lseconds!").send()
                Thread.sleep(Duration.ofSeconds(1))
                i--
            }
            Bukkit.getServer().shutdown()
        }.start()
    }
}