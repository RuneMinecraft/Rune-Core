package com.dank1234.utils.wrapper.player;

import com.dank1234.plugin.Codex
import com.dank1234.plugin.global.economy.Economy
import com.dank1234.plugin.global.ranks.Group
import com.dank1234.plugin.global.ranks.Track
import com.dank1234.utils.Consts
import com.dank1234.utils.data.Database
import com.dank1234.utils.data.database.EcoManager
import com.dank1234.utils.wrapper.inventory.Menu
import com.dank1234.utils.wrapper.item.Item
import com.dank1234.utils.wrapper.location.Location
import com.dank1234.utils.wrapper.message.Message

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent
import org.json.JSONArray

import java.sql.ResultSet
import java.util.*

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
            Codex.getCachedUsers().firstOrNull { it.uuid == uuid }?.let {
                return it
            }

            if (!exists(uuid)!!) {
                Database.SQLUtils.executeUpdate("INSERT INTO users (uuid, username) VALUES (?, ?);") { pstmt ->
                    pstmt.setString(1, uuid.toString())
                    pstmt.setString(2, username)
                }
                Database.SQLUtils.executeUpdate("INSERT INTO user_data (uuid) VALUES (?)") { pstmt ->
                    pstmt.setString(1, uuid.toString())
                }
            }

            return User(uuid, username).apply {
                updateEconomy()
                Codex.addUser(this)
            }
        }
        @JvmStatic fun getUser(uuid: UUID): Optional<User> {
            Codex.getCachedUsers().firstOrNull { it.uuid == uuid }?.let {
                return Optional.of(it)
            }

            val sql = "SELECT * FROM users WHERE uuid = ?"
            return Database.SQLUtils.executeQuery(sql, { pstmt -> pstmt.setString(1, uuid.toString()) }) { rs ->
                if (rs.next()) mapResultSet(rs) else Consts.NULL_USER
            }?.let {
                Codex.addUser(it)
                Optional.of(it)
            }!!
        }
        @JvmStatic fun getUser(username: String): Optional<User> {
            Codex.getCachedUsers().firstOrNull { it.username.equals(username, ignoreCase = true) }?.let {
                return Optional.of(it)
            }

            val sql = "SELECT * FROM users WHERE username = ?"
            return Database.SQLUtils.executeQuery(sql, { pstmt -> pstmt.setString(1, username) }) { rs ->
                if (rs.next()) mapResultSet(rs) else Consts.NULL_USER
            }?.let {
                Codex.addUser(it)
                Optional.of(it)
            }!!
        }

        @JvmStatic fun of(uuid: UUID): User = getUser(uuid).orElse(null)
        @JvmStatic fun of(username: String): User = getUser(username).orElse(null)

        @JvmStatic fun exists(uuid: UUID): Boolean? {
            val sql = "SELECT 1 FROM users WHERE uuid = ? LIMIT 1"
            return Database.SQLUtils.executeQuery(sql, { pstmt -> pstmt.setString(1, uuid.toString()) }) { rs ->
                rs.next()
            };
        }
        @JvmStatic fun exists(username: String): Boolean? {
            val sql = "SELECT 1 FROM users WHERE username = ? LIMIT 1"
            return Database.SQLUtils.executeQuery(sql, { pstmt -> pstmt.setString(1, username) }) { rs ->
                rs.next()
            };
        }

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
        private fun mapResultSet(rs: ResultSet): User {
            val uuid = UUID.fromString(rs.getString("uuid"))
            val username = rs.getString("username")

            val sql = "SELECT * FROM user_data WHERE uuid = ?"
            val userDataRs = Database.SQLUtils.executeQuery(sql, { pstmt -> pstmt.setString(1, uuid.toString()) }) { rs -> rs }

            if (userDataRs?.next() != true) {
                throw IllegalStateException("User data for $username (UUID: $uuid) not found!")
            }

            val groupsJson = userDataRs.getString("groups") ?: "[]"
            val groups = parseGroups(groupsJson)

            val tracksJson = userDataRs.getString("tracks") ?: "[]"
            val tracks = parseTracks(tracksJson)

            val permissionsJson = userDataRs.getString("permissions") ?: "[]"
            val permissions = parsePermissions(permissionsJson)

            return User(uuid, username).apply {
                this.groups.addAll(groups)
                this.tracks.putAll(tracks)
                this.permissions.addAll(permissions)
            }
        }

        private fun parseGroups(groupsJson: String): List<Group> {
            val groupsList = mutableListOf<Group>()
            val groupsArray = JSONArray(groupsJson)
            for (i in 0 until groupsArray.length()) {
                val groupId = groupsArray.getString(i)

                val sql = "SELECT name FROM groups WHERE id = ?"
                val groupName = Database.SQLUtils.executeQuery(sql, { pstmt -> pstmt.setString(1, groupId) }) { rs ->
                    if (rs.next()) rs.getString("name") else null
                }

                groupName?.let {
                    Group.get(it)?.let { group -> groupsList.add(group) }
                }
            }
            return groupsList
        }
        private fun parseTracks(tracksJson: String): Map<Track, Int> {
            val tracksMap = mutableMapOf<Track, Int>()
            val tracksArray = JSONArray(tracksJson)
            for (i in 0 until tracksArray.length()) {
                val trackData = tracksArray.getJSONObject(i)
                val trackId = tracksArray.getString(i)

                val sql = "SELECT name FROM tracks WHERE id = ?"
                val groupName = Database.SQLUtils.executeQuery(sql, { pstmt -> pstmt.setString(1, trackId) }) { rs ->
                    if (rs.next()) rs.getString("name") else null
                }
                val position = trackData.getInt("position")

                groupName?.let {
                    Track.get(it)?.let { track -> tracksMap.put(track, position) }
                }
            }
            return tracksMap
        }
        private fun parsePermissions(permissionsJson: String): List<String> {
            val permissionsList = mutableListOf<String>()
            val permissionsArray = JSONArray(permissionsJson)
            for (i in 0 until permissionsArray.length()) {
                permissionsList.add(permissionsArray.getString(i))
            }
            return permissionsList
        }
    }

    fun sendMessage(vararg messages: String) {
        Message.create(this, *messages).send();
    }

    fun teleport(loc: Location) {
        this.getPlayer().teleportAsync(org.bukkit.Location(
            loc.world,
            loc.x,
            loc.y,
            loc.z,
            loc.yaw,
            loc.pitch
        ), PlayerTeleportEvent.TeleportCause.PLUGIN)
    }
    fun teleport(user: User) {
        this.getPlayer().teleportAsync(org.bukkit.Location(
            user.getLocation().world,
            user.getLocation().x,
            user.getLocation().y,
            user.getLocation().z,
            user.getLocation().yaw,
            user.getLocation().pitch
        ), PlayerTeleportEvent.TeleportCause.PLUGIN)
    }

    fun getLocation(): Location = Location.of(this.getPlayer().location).get()

    fun isOnline(): Boolean = getPlayer().isOnline
    fun gamemode(): GameMode = GameMode.valueOf(getPlayer().gameMode.name)
    fun setGameMode(gm: GameMode): User {
        getPlayer().gameMode = org.bukkit.GameMode.valueOf(gm.name)
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
            if (!Group.exists(group.name)!!) {
                return this
            }
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
    fun clearGroups(): User {
        groups.clear()

        val sql = "UPDATE user_data SET groups = JSON_ARRAY() WHERE uuid = ?"
        Database.SQLUtils.executeUpdate(sql) { pstmt ->
            pstmt.setString(1, uuid.toString())
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
    fun clearTracks(): User {
        tracks.clear()

        val sql = "UPDATE user_data SET tracks = JSON_OBJECT() WHERE uuid = ?"
        Database.SQLUtils.executeUpdate(sql) { pstmt ->
            pstmt.setString(1, uuid.toString())
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
    fun clearPermissions(): User {
        permissions.clear()

        val sql = "UPDATE user_data SET permissions = JSON_ARRAY() WHERE uuid = ?"
        Database.SQLUtils.executeUpdate(sql) { pstmt ->
            pstmt.setString(1, uuid.toString())
        }
        return this
    }

    override fun toString(): String {
        return """
            User[username='$username', uuid='$uuid']
        """.trimIndent()
    }
}