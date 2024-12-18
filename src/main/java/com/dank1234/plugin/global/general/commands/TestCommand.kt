package com.dank1234.plugin.global.general.commands;

import com.dank1234.utils.command.Command;
import com.dank1234.utils.command.ICommand;
import com.dank1234.utils.data.Database;
import com.dank1234.utils.wrapper.player.User;
import org.jetbrains.annotations.NotNull;
import java.sql.PreparedStatement

import java.sql.SQLException;

@Command(names = ["test"])
class TestCommand : ICommand() {
    override fun execute(user: User, vararg args: String) {
        val discordId = Database.SQLUtils.executeQuery(
            "SELECT discord_id FROM users WHERE uuid = ?",
            { statement: PreparedStatement -> statement.setString(1, user.uuid.toString()) },
            { rs ->
                if (rs.next()) {
                    user.sendMessage("&eYour Discord ID: &b${rs.getString("discord_id")}")
                } else {
                    user.sendMessage("&cYou are not synced!")
                }
            }
        )
    }
}