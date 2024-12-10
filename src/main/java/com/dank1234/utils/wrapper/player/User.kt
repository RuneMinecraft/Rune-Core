package com.dank1234.utils.wrapper.player;

import com.dank1234.plugin.global.economy.Economy;
import com.dank1234.utils.data.Database
import com.dank1234.utils.data.database.EcoManager;
import com.dank1234.utils.data.database.UserManager;
import com.dank1234.utils.wrapper.inventory.Menu;
import com.dank1234.utils.wrapper.item.Item;
import com.dank1234.plugin.global.ranks.Group
import com.dank1234.plugin.global.ranks.Track
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

open class User (
    open val uuid: UUID,
    open val username: String,
    private var gems: Double = 0.0,
    private var tokens: Double = 0.0,
    private var souls: Double = 0.0,
    val groups: MutableList<Group> = mutableListOf(),
    val tracks: MutableMap<Track, Int> = mutableMapOf(),
    val permissions: MutableList<String> = mutableListOf()
) {
    companion object {
        @JvmStatic fun of(uuid: UUID, username: String): User {
            if (!UserManager.exists(uuid)) {
                val sql = "INSERT INTO users (uuid, username) VALUES (?, ?)"
                Database.SQLUtils.executeUpdate(sql) { pstmt ->
                    pstmt.setString(1, uuid.toString())
                    pstmt.setString(2, username)
                }
            }
            return User(uuid, username).apply { updateEconomy() }
        }
        @JvmStatic fun of(uuid: UUID): User? = UserManager.getUser(uuid).orElse(null)
        @JvmStatic fun of(username: String): User? = UserManager.getUser(username).orElse(null)

        @JvmStatic fun ensureTables() {
            Database.SQLUtils.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    uuid VARCHAR(36) PRIMARY KEY NOT NULL,
                    username VARCHAR(255) NOT NULL,
                    discord_id VARCHAR(225) DEFAULT '0'
                );
            """.trimIndent())

            Database.SQLUtils.executeUpdate("""
                CREATE TABLE IF NOT EXISTS groups (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL UNIQUE,
                    weight INT NOT NULL DEFAULT 0,
                    prefix VARCHAR(255) DEFAULT '',
                    suffix VARCHAR(255) DEFAULT NULL,
                    inherited_groups JSON DEFAULT '[]'
                );
            """.trimIndent())

            Database.SQLUtils.executeUpdate("""
                CREATE TABLE IF NOT EXISTS tracks (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    groups JSON DEFAULT '[]'
                );
            """.trimIndent())

            Database.SQLUtils.executeUpdate("""
                CREATE TABLE IF NOT EXISTS user_data (
                    uuid VARCHAR(36) PRIMARY KEY NOT NULL,
                    groups JSON DEFAULT '[]',
                    permissions JSON DEFAULT '[]',
                    tracks JSON DEFAULT '[]',
                    FOREIGN KEY (uuid) REFERENCES users(uuid) ON DELETE CASCADE
                );
            """.trimIndent())
        }
    }

    fun isOnline(): Boolean = getPlayer().isOnline
    fun gamemode(): GameMode = getPlayer().gameMode
    fun setGameMode(gm: GameMode): User {
        getPlayer().gameMode = gm
        return this
    }
    fun sudo(cmd: String): User {
        if (cmd.startsWith("c://") || cmd.startsWith("C://")) {
            getPlayer().chat(cmd.substring(4))
        } else {
            getPlayer().performCommand(cmd)
        }
        return this
    }

    fun getEco(eco: Economy): Double = when (eco) {
        Economy.GEMS -> gems
        Economy.SOULS -> souls
        Economy.TOKENS -> tokens
    }
    fun setEco(eco: Economy, value: Double): User {
        when (eco) {
            Economy.GEMS -> gems = value
            Economy.TOKENS -> tokens = value
            Economy.SOULS -> souls = value
        }
        EcoManager.setValue(uuid, eco.name, value)
        return this
    }

    fun gems(): Double = gems
    fun setGems(gems: Double): User {
        this.gems = gems
        EcoManager.setValue(uuid, "gems", gems)
        return this
    }

    fun tokens(): Double = tokens
    fun setTokens(tokens: Double): User {
        this.tokens = tokens
        EcoManager.setValue(uuid, "tokens", tokens)
        return this
    }

    fun souls(): Double = souls
    fun setSouls(souls: Double): User {
        this.souls = souls
        EcoManager.setValue(uuid, "souls", souls)
        return this
    }

    fun updateEconomy(): User {
        gems = EcoManager.getValue(uuid, "gems").orElse(0.0)
        tokens = EcoManager.getValue(uuid, "tokens").orElse(0.0)
        souls = EcoManager.getValue(uuid, "souls").orElse(0.0)
        return this
    }

    fun openMenu(menu: Menu): Menu {
        getPlayer().openInventory(menu.inventory())
        return menu
    }
    fun closeMenu(): User {
        getPlayer().closeInventory()
        return this
    }

    fun getHeldItem(): Item? {
    val itemStack = getPlayer().inventory.itemInMainHand
            return if (itemStack.type == Material.AIR) null else Item.of(itemStack)
        }
    fun giveItem(item: Item): User {
        getPlayer().inventory.addItem(item.toBukkit())
        return this
    }

    fun getPlayer(): Player {
        return Bukkit.getPlayer(uuid) ?: throw IllegalStateException("Player is not online. Cannot retrieve player.")
    }
    fun getCommandSender(): CommandSender {
        return Bukkit.getPlayer(uuid) ?: throw IllegalStateException("Player is not online. Cannot retrieve command sender.")
    }

    fun addGroup(group: Group): User {
        if (!groups.contains(group)) {
            groups.add(group)
            val sql = "UPDATE user_data SET groups = JSON_ARRAY_APPEND(groups, '$', ?) WHERE uuid = ?"
            Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setString(1, group.id.toString())
                pstmt.setString(2, uuid.toString())
            }
        }
        return this
    }
    fun removeGroup(group: Group): User {
        if (groups.remove(group)) {
            val sql = "UPDATE user_data SET groups = JSON_REMOVE(groups, JSON_UNQUOTE(JSON_SEARCH(groups, 'one', ?))) WHERE uuid = ?"
            Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setString(1, group.id.toString())
                pstmt.setString(2, uuid.toString())
            }
        }
        return this
    }
    fun addTrack(track: Track, position: Int): User {
        if (!tracks.containsKey(track)) {
            tracks[track] = position
            val sql = "UPDATE user_data SET tracks = JSON_SET(tracks, ?, ?) WHERE uuid = ?"
            Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setString(1, "$.\"${track.id}\"")
                pstmt.setInt(2, position)
                pstmt.setString(3, uuid.toString())
            }
        }
        return this
    }
    fun removeTrack(track: Track): User {
        if (tracks.remove(track) != null) {
            val sql = "UPDATE user_data SET tracks = JSON_REMOVE(tracks, ?) WHERE uuid = ?"
            Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setString(1, "$.\"${track.id}\"")
                pstmt.setString(2, uuid.toString())
            }
        }
        return this
    }
    fun addPermission(permission: String): User {
        if (!permissions.contains(permission)) {
            permissions.add(permission)
            val sql = "UPDATE user_data SET permissions = JSON_ARRAY_APPEND(permissions, '$', ?) WHERE uuid = ?"
            Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setString(1, permission)
                pstmt.setString(2, uuid.toString())
            }
        }
        return this
    }
    fun removePermission(permission: String): User {
        if (permissions.remove(permission)) {
            val sql = "UPDATE user_data SET permissions = JSON_REMOVE(permissions, JSON_UNQUOTE(JSON_SEARCH(permissions, 'one', ?))) WHERE uuid = ?"
            Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setString(1, permission)
                pstmt.setString(2, uuid.toString())
            }
        }
        return this
    }

    override fun toString(): String {
        return """
            User[username='$username', uuid='$uuid']
        """.trimIndent()
    }
}