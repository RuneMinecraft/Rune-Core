package com.dank1234.utils.wrapper.player

import com.dank1234.plugin.Codex
import com.dank1234.plugin.global.economy.Economy
import com.dank1234.plugin.global.ranks.Group
import com.dank1234.plugin.global.ranks.Track
import com.dank1234.utils.Consts
import com.dank1234.utils.Result
import com.dank1234.utils.data.Database
import com.dank1234.utils.data.database.EcoManager
import com.dank1234.utils.wrapper.inventory.Menu
import com.dank1234.utils.wrapper.item.Item
import com.dank1234.utils.wrapper.location.Location
import com.dank1234.utils.wrapper.message.Message
import com.dank1234.utils.command.Command

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent
import org.json.JSONArray

import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

@Suppress("NAME_SHADOWING", "UNUSED")
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
    /**
     * Holds static objects for the class also so be annotated by [@JvmStatic]
     * @author dank1234
     */
    companion object {
        /**
         * Creates a [User] if they do not already exist in the [Database].
         * @param uuid The users minecraft unique id.
         * @param username The users minecraft in-game name.
         *
         * @return The [User] objet that was inserted into the [Database].
         * @author dank1234
         */
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

            return of(uuid).apply {
                updateEconomy()
                Codex.addUser(this)
            }
        }

        /**
         * Retrieves a [User] object that may or may not be null by their unique id.
         * @param uuid The unique id used to retrieve the user.
         *
         * @return Either a null or a [User] object handled by the [Optional] java class.
         * @author dank1234
         */
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
        /**
         * Retrieves a [User] object that may or may not be null by their username.
         * @param username The username used to retrieve the user.
         *
         * @return Either a null or a [User] object handled by the [Optional] java class.
         * @author dank1234
         */
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

        /**
         * Retrieves a [User] with no null handling, in case it wants to be manually handled.
         * @param uuid The unique id used to retrieve the user.
         *
         * @return Either a null or a [User] object that is not null-handled.
         * @author dank1234
         */
        @JvmStatic fun of(uuid: UUID): User = getUser(uuid).orElse(null)
        /**
         * Retrieved a [User] with no null handling, in case it wants to be manually handled.
         * @param username The username used to retrieve the user.
         *
         * @return Either a null or a [User] object that is not null-handled.
         * @author dank1234
         */
        @JvmStatic fun of(username: String): User = getUser(username).orElse(null)

        /**
         * Checks if a user exists in the [Database].
         * @param uuid The unique id to match in the database.
         *
         * @return A [Boolean] based on weather the user exists.
         * @author dank1234
         */
        @JvmStatic fun exists(uuid: UUID): Boolean? {
            val sql = "SELECT 1 FROM users WHERE uuid = ? LIMIT 1"
            return Database.SQLUtils.executeQuery(sql, { pstmt -> pstmt.setString(1, uuid.toString()) }) { rs ->
                rs.next()
            }
        }
        /**
         * Checks if a user exists in the [Database].
         * @param username The username to match in the database.
         *
         * @return A [Boolean] based on weather the user exists.
         * @author dank1234
         */
        @JvmStatic fun exists(username: String): Boolean? {
            val sql = "SELECT 1 FROM users WHERE username = ? LIMIT 1"
            return Database.SQLUtils.executeQuery(sql, { pstmt -> pstmt.setString(1, username) }) { rs ->
                rs.next()
            }
        }

        /**
         * Ensures all the [User] related tables are correctly made in the [Database].
         *
         * @return [Result]
         * @author dank1234
         */
        @JvmStatic fun ensureTables(): Result {
            try {
                Database.SQLUtils.executeUpdate(
                    """
                CREATE TABLE IF NOT EXISTS users (
                    uuid VARCHAR(36) PRIMARY KEY NOT NULL,
                    username VARCHAR(255) NOT NULL,
                    discord_id VARCHAR(225) DEFAULT '0'
                );
            """.trimIndent()
                )
                Database.SQLUtils.executeUpdate(
                    """
                CREATE TABLE IF NOT EXISTS groups (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL UNIQUE,
                    weight INT NOT NULL DEFAULT 0,
                    prefix VARCHAR(255) DEFAULT '',
                    suffix VARCHAR(255) DEFAULT NULL,
                    inherited_groups JSON DEFAULT '[]'
                );
            """.trimIndent()
                )
                Database.SQLUtils.executeUpdate(
                    """
                CREATE TABLE IF NOT EXISTS tracks (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    groups JSON DEFAULT '[]'
                );
            """.trimIndent()
                )
                Database.SQLUtils.executeUpdate(
                    """
                CREATE TABLE IF NOT EXISTS user_data (
                    uuid VARCHAR(36) PRIMARY KEY NOT NULL,
                    chat_colour VARCHAR(225) DEFAULT,
                    groups JSON DEFAULT '[]',
                    permissions JSON DEFAULT '[]',
                    tracks JSON DEFAULT '[]',
                    FOREIGN KEY (uuid) REFERENCES users(uuid) ON DELETE CASCADE
                );
            """.trimIndent()
                )
                return Result.SUCCESSFUL
            }catch (e: SQLException) {
                e.printStackTrace()
                return Result.EXCEPTION
            }
        }

        /**
         * Creates the [User] object for all creation methods. It links all [Database] tables
         * related to user data, such as groups, users and user_data.
         * @param rs The [ResultSet] (Data from the [Database]) to be turned into a [User].
         *
         * @return The [User] object created with all info from the [Database]
         * @author dank1234
         */
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

        /**
         * Collect all groups from the database from the [JSONArray].
         * @param groupsJson The [JSONArray] from the user_data table in the [Database].
         *
         * @return A List of the [Group] object.
         * @author dank1234
         */
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
        /**
         * Collect all tracks from the database from the [JSONArray].
         * @param tracksJson The [JSONArray] from the user_data table in the [Database].
         *
         * @return A List of the [Track] object.
         * @author dank1234
         */
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
        /**
         * Collect all permission from the database from the [JSONArray].
         * @param permissionsJson The [JSONArray] from the user_data table in the [Database].
         *
         * @return A List of the [String] object.
         * @author dank1234
         */
        private fun parsePermissions(permissionsJson: String): List<String> {
            val permissionsList = mutableListOf<String>()
            val permissionsArray = JSONArray(permissionsJson)
            for (i in 0 until permissionsArray.length()) {
                permissionsList.add(permissionsArray.getString(i))
            }
            return permissionsList
        }
    }

    /**
     * Ensures all of a user's groups, tracks and permissions are all in order.
     *
     * @author dank1234
     */
    init {
        this.sortGroups()
        this.sortTracks()
        this.sortPermissions()
    }

    /**
     * Send a [Message] to the [User].
     * @param messages A vararg ([String] Array) of messages to send to the [User].
     *
     * @return [Result]
     * @author dank1234
     */
    fun sendMessage(vararg messages: String): Result {
        try {
            Message.create(this, *messages).send()
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }

    /**
     * Teleports the [User] to a different [Location].
     * @param loc The [Location] they will be teleported to.
     * 
     * @return [Result]
     * @author dank1234
     */
    fun teleport(loc: Location): Result {
        try {
            this.getPlayer().teleportAsync(
                org.bukkit.Location(
                    loc.world,
                    loc.x,
                    loc.y,
                    loc.z,
                    loc.yaw,
                    loc.pitch
                ), PlayerTeleportEvent.TeleportCause.PLUGIN
            )
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }
    /**
     * Teleports the [User] to a different [Location].
     * @param user The [User]'s [Location] they will be teleported to.
     *
     * @return [Result]
     * @author dank1234
     */
    fun teleport(user: User): Result {
        try {
            this.getPlayer().teleportAsync(
                org.bukkit.Location(
                    user.getLocation().world,
                    user.getLocation().x,
                    user.getLocation().y,
                    user.getLocation().z,
                    user.getLocation().yaw,
                    user.getLocation().pitch
                ), PlayerTeleportEvent.TeleportCause.PLUGIN
            )
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }

    /**
     * Retrieves the [Location] of the user.
     *
     * @return The location of the [User].
     * @author dank1234
     */
    fun getLocation(): Location = Location.of(this.getPlayer().location).get()

    /**
     * Checks if the [User] is currently online.
     *
     * @return A [Boolean] value of whether the [User] is online or not.
     * @author dank1234
     */
    fun isOnline(): Boolean = getPlayer().isOnline
    /**
     * Returns the [GameMode] the [User] is currently in.
     *
     * @return A [GameMode] depending on the [User]'s current state.
     * @author dank1234
     */
    fun gamemode(): GameMode = GameMode.valueOf(getPlayer().gameMode.name)
    /**
     * Changes the [GameMode] the [User] is currently in.
     *
     * @return [Result]
     * @author dank1234
     */
    fun setGameMode(gm: GameMode): Result {
        try {
            getPlayer().gameMode = org.bukkit.GameMode.valueOf(gm.name)
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }
    /**
     * Forces the [User] to run a [Command] or send a [Message].
     * @param cmd A string of either the [Command] / [Message] for the [User] to execute / send.
     *
     * @return [Result]
     * @author dank1234
     */
    fun sudo(cmd: String): Result {
        try {
            if (cmd.startsWith("c://") || cmd.startsWith("C://")) {
                getPlayer().chat(cmd.substring(4))
            } else {
                getPlayer().performCommand(cmd)
            }
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }

    /**
     * Retrieves the [User]'s current balance in [Double] of an [Economy].
     * @param eco The [Economy] that will be retrieved.
     *
     * @return The balance of the [Economy] in a [Double].
     * @author dank1234
     */
    fun getEco(eco: Economy): Double = when (eco) {
        Economy.GEMS -> gems
        Economy.SOULS -> souls
        Economy.TOKENS -> tokens
    }
    /**
     * Sets the balance of an [Economy] for the [User].
     * @param eco The [Economy] that will be updated.
     * @param value The new balance of the [Economy] in a [Double].
     *
     * @return [Result]
     * @author dank1234
     */
    fun setEco(eco: Economy, value: Double): Result {
        try {
            when (eco) {
                Economy.GEMS -> gems = value
                Economy.TOKENS -> tokens = value
                Economy.SOULS -> souls = value
            }
            EcoManager.setValue(uuid, eco.name, value)
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }

    /**
     *
     */
    fun setGems(gems: Double): Result {
        try {
            this.gems = gems
            EcoManager.setValue(uuid, "gems", gems)
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }

    /**
     *
     */
    fun tokens(): Double = tokens
    /**
     *
     */
    fun setTokens(tokens: Double): Result {
        try {
            this.tokens = tokens
            EcoManager.setValue(uuid, "tokens", tokens)
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }

    /**
     *
     */
    fun souls(): Double = souls
    /**
     *
     */
    fun setSouls(souls: Double): Result {
        try {
            this.souls = souls
            EcoManager.setValue(uuid, "souls", souls)
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }

    /**
     *
     */
    fun updateEconomy(): Result {
        try {
            gems = EcoManager.getValue(uuid, "gems").orElse(0.0)
            tokens = EcoManager.getValue(uuid, "tokens").orElse(0.0)
            souls = EcoManager.getValue(uuid, "souls").orElse(0.0)
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }

    /**
     *
     */
    fun openMenu(menu: Menu): Menu {
        getPlayer().openInventory(menu.inventory())
        return menu
    }
    /**
     *
     */
    fun closeMenu(): Result {
        try {
            getPlayer().closeInventory()
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }

    /**
     *
     */
    fun getHeldItem(): Item? {
    val itemStack = getPlayer().inventory.itemInMainHand
            return if (itemStack.type == Material.AIR) null else Item.of(itemStack)
        }
    /**
     *
     */
    fun giveItem(item: Item): Result {
        try {
            getPlayer().inventory.addItem(item.toBukkit())
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }

    /**
     *
     */
    fun getPlayer(): Player {
        return Bukkit.getPlayer(uuid) ?: throw IllegalStateException("Player is not online. Cannot retrieve player.")
    }
    /**
     *
     */
    fun getCommandSender(): CommandSender {
        return Bukkit.getPlayer(uuid) ?: throw IllegalStateException("Player is not online. Cannot retrieve command sender.")
    }

    /**
     *
     */
    fun addGroup(group: Group): Result {
        try {
            if (!groups.contains(group)) {
                if (!Group.exists(group.name)!!) {
                    return Result.FAILURE
                }
                groups.add(group)
                this.sortGroups()
                val sql = "UPDATE user_data SET groups = JSON_ARRAY_APPEND(groups, '$', ?) WHERE uuid = ?"
                Database.SQLUtils.executeUpdate(sql) { pstmt ->
                    pstmt.setString(1, group.id.toString())
                    pstmt.setString(2, uuid.toString())
                }
            }
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }
    /**
     *
     */
    fun removeGroup(group: Group): Result {
        try {
            if (groups.remove(group)) {
                val sql = "UPDATE user_data SET groups = JSON_REMOVE(groups, JSON_UNQUOTE(JSON_SEARCH(groups, 'one', ?))) WHERE uuid = ?"
                Database.SQLUtils.executeUpdate(sql) { pstmt ->
                    pstmt.setString(1, group.id.toString())
                    pstmt.setString(2, uuid.toString())
                }
            }
            this.sortGroups()
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }
    /**
     *
     */
    fun clearGroups(): Result {
        try{
            groups.clear()

            val sql = "UPDATE user_data SET groups = JSON_ARRAY() WHERE uuid = ?"
            Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setString(1, uuid.toString())
            }
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }

    /**
     *
     */
    fun addTrack(track: Track, position: Int): Result {
        try {
            if (!tracks.containsKey(track)) {
                tracks[track] = position
                this.sortTracks()
                val sql = "UPDATE user_data SET tracks = JSON_SET(tracks, ?, ?) WHERE uuid = ?"
                Database.SQLUtils.executeUpdate(sql) { pstmt ->
                    pstmt.setString(1, "$.\"${track.id}\"")
                    pstmt.setInt(2, position)
                    pstmt.setString(3, uuid.toString())
                }
            }else{
                return Result.FAILURE
            }
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }
    /**
     *
     */
    fun removeTrack(track: Track): Result {
        try {
            if (tracks.remove(track) != null) {
                val sql = "UPDATE user_data SET tracks = JSON_REMOVE(tracks, ?) WHERE uuid = ?"
                Database.SQLUtils.executeUpdate(sql) { pstmt ->
                    pstmt.setString(1, "$.\"${track.id}\"")
                    pstmt.setString(2, uuid.toString())
                }
            }
            this.sortTracks()
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }
    /**
     *
     */
    fun clearTracks(): Result {
        try {
            tracks.clear()

            val sql = "UPDATE user_data SET tracks = JSON_OBJECT() WHERE uuid = ?"
            Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setString(1, uuid.toString())
            }
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }

    /**
     *
     */
    fun addPermission(permission: String): Result {
        try {
            if (!permissions.contains(permission)) {
                permissions.add(permission)
                this.sortPermissions()
                val sql = "UPDATE user_data SET permissions = JSON_ARRAY_APPEND(permissions, '$', ?) WHERE uuid = ?"
                Database.SQLUtils.executeUpdate(sql) { pstmt ->
                    pstmt.setString(1, permission)
                    pstmt.setString(2, uuid.toString())
                }
            }
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }
    /**
     *
     */
    fun removePermission(permission: String): Result {
        try {
            if (permissions.remove(permission)) {
                val sql = "UPDATE user_data SET permissions = JSON_REMOVE(permissions, JSON_UNQUOTE(JSON_SEARCH(permissions, 'one', ?))) WHERE uuid = ?"
                Database.SQLUtils.executeUpdate(sql) { pstmt ->
                    pstmt.setString(1, permission)
                    pstmt.setString(2, uuid.toString())
                }
            }
            this.sortPermissions()
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }
    /**
     *
     */
    fun clearPermissions(): Result {
        try {
            permissions.clear()

            val sql = "UPDATE user_data SET permissions = JSON_ARRAY() WHERE uuid = ?"
            Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setString(1, uuid.toString())
            }
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }

    /**
     *
     */
    private fun sortGroups(): Result {
        try {
            groups.sortBy { it.weight }
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }

    }
    /**
     *
     */
    private fun sortTracks(): Result {
        try {
            val sortedTracks = tracks.entries
                .sortedBy { it.key.name }
                .associate { it.key to it.value }
            tracks.clear()
            tracks.putAll(sortedTracks)
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }
    /**
     *
     */
    private fun sortPermissions(): Result {
        try {
            permissions.sort()
            return Result.SUCCESSFUL
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }

    /**
     *
     */
    override fun toString(): String {
        return """
            User[
                username=$username, 
                uuid=$uuid
            ]
        """.trimIndent()
    }
}