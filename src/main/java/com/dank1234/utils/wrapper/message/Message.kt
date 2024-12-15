package com.dank1234.utils.wrapper.message;

import com.dank1234.utils.Logger;
import com.dank1234.utils.Utils;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import net.kyori.adventure.text.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

class Message private constructor(
    private val type: MessageType,
    input: Set<*>,
    private val messages: Array<String> = emptyArray(),
    private val cMessages: Array<Component> = emptyArray()
) : Utils {
    private val players: MutableSet<CommandSender> = mutableSetOf()
    private val users: MutableSet<User> = mutableSetOf()

    init {
        if (input.isNotEmpty()) {
            val first = input.first()
            when (first) {
                is CommandSender -> input.forEach {
                    val player = it as CommandSender
                    players.add(player)
                    User.of(player.name).let { it1 -> users.add(it1) }
                }
                is User -> input.forEach {
                    val user = it as User
                    users.add(user)
                    players.add(user.getPlayer())
                }
            }
        }
    }

    companion object {
        @JvmStatic fun create(type: MessageType, player: CommandSender, vararg messages: String): Message {
            return create(type, arrayOf(player), *messages)
        }
        @JvmStatic fun create(player: CommandSender, vararg messages: String): Message {
            return create(MessageType.NORMAL, player, *messages)
        }
        @JvmStatic fun create(type: MessageType, players: Array<CommandSender>, vararg messages: String): Message {
            return Message(type, players.toSet(), messages.toList().toTypedArray())
        }
        @JvmStatic fun create(players: Array<CommandSender>, vararg messages: String): Message {
            return create(MessageType.NORMAL, players, *messages)
        }
        @JvmStatic fun create(type: MessageType, player: CommandSender, vararg messages: Component): Message {
            return create(type, arrayOf(player), *messages)
        }
        @JvmStatic fun create(player: CommandSender, vararg messages: Component): Message {
            return create(MessageType.NORMAL, player, *messages)
        }
        @JvmStatic fun create(type: MessageType, players: Array<CommandSender>, vararg messages: Component): Message {
            return Message(type, players.toSet(), cMessages = messages.toList().toTypedArray())
        }
        @JvmStatic fun create(players: Array<CommandSender>, vararg messages: Component): Message {
            return create(MessageType.NORMAL, players, *messages)
        }
        @JvmStatic fun create(type: MessageType, user: User, vararg messages: String): Message {
            return create(type, arrayOf(user), *messages)
        }
        @JvmStatic fun create(user: User, vararg messages: String): Message {
            return create(MessageType.NORMAL, user, *messages)
        }
        @JvmStatic fun create(type: MessageType, users: Array<User>, vararg messages: String): Message {
            return Message(type, users.toSet(), messages = messages.toList().toTypedArray())
        }
        @JvmStatic fun create(users: Array<User>, vararg messages: String): Message {
            return create(MessageType.NORMAL, users, *messages)
        }
        @JvmStatic fun create(type: MessageType, user: User, vararg messages: Component): Message {
            return create(type, arrayOf(user), *messages)
        }
        @JvmStatic fun create(user: User, vararg messages: Component): Message {
            return create(MessageType.NORMAL, user, *messages)
        }
        @JvmStatic fun create(type: MessageType, users: Array<User>, vararg messages: Component): Message {
            return Message(type, users.toSet(), cMessages = messages.toList().toTypedArray())
        }
        @JvmStatic fun create(users: Array<User>, vararg messages: Component): Message {
            return create(MessageType.NORMAL, users, *messages)
        }
        @JvmStatic fun broadcast(type: MessageType, vararg messages: String): Message {
            return create(type, Bukkit.getOnlinePlayers().toTypedArray(), *messages)
        }
        @JvmStatic fun broadcast(vararg messages: String): Message {
            return broadcast(MessageType.NORMAL, *messages)
        }
        @JvmStatic fun broadcast(type: MessageType, vararg messages: Component): Message {
            return create(type, Bukkit.getOnlinePlayers().toTypedArray(), *messages)
        }
        @JvmStatic fun broadcast(vararg messages: Component): Message {
            return broadcast(MessageType.NORMAL, *messages)
        }
    }

    fun send() {
        if (players.isEmpty() || users.isEmpty()) {
            Logger.log("Failed to send a message. 'Set<CommandSender> players' is empty.")
            return
        }
        messages.forEach { message ->
            players.forEach { player ->
                player.sendMessage(Colour("$type$message"))
            }
        }
        cMessages.forEach { cMessage ->
            players.forEach { player ->
                player.sendMessage(cMessage)
            }
        }
    }
    fun send(log: Boolean) {
        send()
        if (log) {
            Logger.log(toString())
        }
    }

    override fun toString(): String {
        val playersNames = players.joinToString { it.name }
        return """
            Message[
                type=$type,
                messages=$messages
            ]
        """.trimIndent()
    }
}