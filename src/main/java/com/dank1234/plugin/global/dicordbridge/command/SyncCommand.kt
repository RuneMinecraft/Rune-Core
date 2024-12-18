package com.dank1234.plugin.global.dicordbridge.command

import com.dank1234.api.command.Command
import com.dank1234.api.command.ICommand
import com.dank1234.api.data.Database
import com.dank1234.api.wrapper.message.Message
import com.dank1234.api.wrapper.player.User
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent

@Command(names=["sync"])
class SyncCommand : ICommand() {
    override fun execute(user: User, vararg args: String) {
        if (user.isSynced()) {
            user.sendMessage("&cYou are already synced!")
            return
        }
        if (user.hasSyncCode()) {
            Message.create(user,
                Component.text(Colour("&eYour sync code: &a${user.getSyncCode()} &e| ")).append(Component.text(Colour("&a&lCopy "))
                    .clickEvent(ClickEvent.copyToClipboard(user.getSyncCode()))
                    .hoverEvent(HoverEvent.showText(Component.text(Colour("&aClick to copy.")))))
                    .append(Component.text(Colour("&e|")))
            ).send()
            return
        }

        val syncCode = generateSyncCode()

        Message.create(user,
            Component.text(Colour("&eYour sync code: &a$syncCode &e| ")).append(Component.text(Colour("&a&lCopy "))
                .clickEvent(ClickEvent.copyToClipboard(syncCode))
                .hoverEvent(HoverEvent.showText(Component.text(Colour("&aClick to copy.")))))
                .append(Component.text(Colour("&e|")))
        ).send()

        Database.SQLUtils.executeUpdate("""
            CREATE TABLE IF NOT EXISTS sync_codes (
                uuid        VARCHAR(36)     PRIMARY KEY NOT NULL,
                code        VARCHAR(225)    NOT NULL,
                FOREIGN KEY (uuid) REFERENCES users(uuid) ON DELETE CASCADE
            );
        """.trimIndent())
        Database.SQLUtils.executeUpdate("INSERT INTO sync_codes (uuid, code) VALUES (?, ?)") { statement ->
            statement.setString(1, user.uuid.toString())
            statement.setString(2, syncCode)
        }
    }

    private fun generateSyncCode(): String {
        val charPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..8)
            .map { charPool.random() }
            .joinToString("")
    }
}