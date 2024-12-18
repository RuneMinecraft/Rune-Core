package com.dank1234.plugin.global.development.commands

import com.dank1234.api.Locale
import com.dank1234.api.command.Command
import com.dank1234.api.command.ICommand
import com.dank1234.api.data.Database
import com.dank1234.api.wrapper.player.User

import java.sql.SQLException

@Command(names = ["dev/sql", "dev/run-sql", "dev/query", "dev/database"])
class SQLCommand : ICommand(){
    override fun execute(user: User, vararg args: String) {
        // TODO: Permission Check! (rune.dev.sql)

        if (args.isEmpty()) {
            user.sendMessage("&cCannot execute a null query.")
            return
        }
        val sql = args.joinToString(" ")

        try {
            if (sql.trim().uppercase().startsWith("SELECT")) {
                val result = Database.SQLUtils.executeQuery(sql, {}, { rs ->
                    val builder = StringBuilder()
                    val metaData = rs.metaData
                    val columnCount = metaData.columnCount

                    val columnNames = (1..columnCount).map { metaData.getColumnName(it) }
                    val columnWidths = columnNames.map { it.length }.toMutableList()

                    val rows = mutableListOf<List<String>>()

                    while (rs.next()) {
                        val row = (1..columnCount).map { i ->
                            val value = rs.getString(i) ?: "NULL"
                            if (value.length > columnWidths[i - 1]) {
                                columnWidths[i - 1] = value.length
                            }
                            value
                        }
                        rows.add(row)
                    }

                    if (rows.isEmpty()) {
                        return@executeQuery "&cNo results found."
                    }

                    val headerRow = columnNames.mapIndexed { i, name ->
                        name.padEnd(columnWidths[i])
                    }.joinToString(" | ", "| ", " |")
                    val separatorRow = columnWidths.joinToString("-+-", "+-", "-+") { "-".repeat(it) }

                    builder.append(headerRow).append("\n")
                    builder.append(separatorRow).append("\n")

                    rows.forEach { row ->
                        val dataRow = row.mapIndexed { i, value ->
                            value.padEnd(columnWidths[i])
                        }.joinToString(" | ", "| ", " |")
                        builder.append(dataRow).append("\n")
                    }

                    builder.toString()
                })

                if (result.isNullOrBlank()) {
                    user.sendMessage("&cNo results found.")
                } else {
                    user.sendMessage(result)
                }
            } else {
                val rowsAffected = Database.SQLUtils.executeUpdate(sql)
                if (rowsAffected > 0) {
                    user.sendMessage("&aQuery executed successfully. Rows affected: &f$rowsAffected")
                } else {
                    user.sendMessage("&cQuery execution failed. No rows were affected.")
                }
            }
        }catch (e: SQLException) {
            e.printStackTrace()
            e.message?.let { user.sendMessage(Locale.EXCEPTION_THROWN, it) }
        }
    }
}