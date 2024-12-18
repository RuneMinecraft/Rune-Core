package com.dank1234.api.wrapper.player

import com.dank1234.plugin.Codex
import com.dank1234.plugin.global.chat.Colour
import com.dank1234.plugin.global.economy.Economy
import com.dank1234.plugin.global.ranks.Group
import com.dank1234.plugin.global.ranks.Track
import com.dank1234.api.Consts
import com.dank1234.api.Result
import com.dank1234.api.data.Database
import com.dank1234.api.data.database.EcoManager
import com.dank1234.api.wrapper.inventory.Menu
import com.dank1234.api.wrapper.item.Item
import com.dank1234.api.wrapper.location.Location
import com.dank1234.api.wrapper.message.Message
import com.dank1234.api.command.Command

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.OfflinePlayer
import org.bukkit.event.player.PlayerTeleportEvent
import org.json.JSONArray
import java.sql.PreparedStatement

import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

/**
 * A [User] is a version of the Minecraft [Player], which allows developers to easily interact with the in-game [Player].
 * It abstracts certain functions such as sending messages, to allow for ease of use and serialization through a database,
 * so that developers can also access the [User] whilst offline, which cannot be done in [Bukkit] without the [OfflinePlayer]
 * class.
 *
 * @param uuid The unique id of the minecraft player.
 * @param username The in-game name of the minecraft player.
 * @param nickname The user's in game nick.
 * @param gems The balance of gems the [User] has.
 * @param tokens The balance of tokens the [User] has.
 * @param souls The balance of souls the [User] has.
 * @param groups A [List] of groups.
 * @param tracks A [Map] of tracks linking to the [User]'s position in them.
 * @param permissions A [List] of strings relating to the permissions the [User] itself has been given.
 *
 * @see Location
 * @see Item
 * @see Menu
 * @see Economy
 * @see Group
 * @see Track
 *
 * @author dank1234
 */
@Suppress("NAME_SHADOWING", "UNUSED")
open class User (
    open val uuid: UUID,
    open val username: String,
    private var nickname: String = "",
    private var chatColour: String = "&f",
    private var gems: Double = 0.0,
    private var tokens: Double = 0.0,
    private var souls: Double = 0.0,
    val groups: MutableList<Group> = mutableListOf(),
    val tracks: MutableMap<Track, Int> = mutableMapOf(),
    val permissions: MutableList<String> = mutableListOf(),
    var hasJoinedBefore: Boolean = false
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
         * @throws SQLException
         * @author dank1234
         */
        @JvmStatic fun of(uuid: UUID, username: String): User {
            Codex.getCachedUsers().firstOrNull { it.uuid == uuid }?.let {
                it.hasJoinedBefore = true
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
            } else {
                return of(uuid).apply {
                    updateEconomy()
                    Codex.addUser(this)
                    hasJoinedBefore = true
                }
            }

            return of(uuid).apply {
                updateEconomy()
                Codex.addUser(this)
                hasJoinedBefore = false
            }
        }

        /**
         * Retrieves a [User] object that may or may not be null by their unique id.
         * @param uuid The unique id used to retrieve the user.
         *
         * @return Either a null or a [User] object handled by the [Optional] java class.
         * @throws SQLException
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
                it.hasJoinedBefore = false
                Codex.addUser(it)
                Optional.of(it)
            }!!
        }
        /**
         * Retrieves a [User] object that may or may not be null by their username.
         * @param username The username used to retrieve the user.
         *
         * @return Either a null or a [User] object handled by the [Optional] java class.
         * @throws SQLException
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
         * @throws NullPointerException
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
         * @throws SQLException
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
         * @throws SQLException
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
         * @throws SQLException
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
                    chat_colour VARCHAR(225) DEFAULT '&f',
                    nickname VARCHAR(225) DEFAULT '',
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
         * @throws SQLException
         * @throws IllegalStateException
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

            val nick = userDataRs.getString("nickname")
            val chatColour = userDataRs.getString("chat_colour")

            val groups = parseGroups(userDataRs.getString("groups") ?: "[]")
            val tracks = parseTracks(userDataRs.getString("tracks") ?: "[]")
            val permissions = parsePermissions( userDataRs.getString("permissions") ?: "[]")

            return User(uuid, username).apply {
                this.nickname = nick
                this.chatColour = chatColour
                this.hasJoinedBefore = true
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
         * @throws SQLException
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
         * @return A [Map] of the [Track] object linked to an [Int] based on the [User]'s position in the [Track].
         * @throws SQLException
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
         * @throws SQLException
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
     * @throws IllegalStateException
     * @author dank1234
     */
    fun getLocation(): Location = Location.of(this.getPlayer().location).get()

    /**
     * Checks if the [User] is currently online.
     *
     * @return A [Boolean] value of whether the [User] is online or not.
     * @throws IllegalStateException
     * @author dank1234
     */
    fun isOnline(): Boolean = getPlayer().isOnline
    /**
     * Returns the [GameMode] the [User] is currently in.
     *
     * @return A [GameMode] depending on the [User]'s current state.
     * @throws IllegalStateException
     * @author dank1234
     */
    fun gamemode(): GameMode = GameMode.valueOf(getPlayer().gameMode.name)
    /**
     * Changes the [GameMode] the [User] is currently in.
     *
     * @return [Result]
     * @throws IllegalStateException
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
     * @throws IllegalStateException
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
     * @throws NullPointerException
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
     * Updates the values of each [Economy] variable in the [User].
     *
     * @return [Result]
     * @throws SQLException
     * @author dank1234
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
     * Opens a [Menu] for the [User].
     * @param menu The [Menu] that the [User] is opening.
     *
     * @return The open [Menu].
     * @throws IllegalStateException
     * @author dank1234
     */
    fun openMenu(menu: Menu): Menu {
        getPlayer().openInventory(menu.inventory())
        return menu
    }
    /**
     * Closes the currently open [Menu] for the [User].
     *
     * @return [Result]
     * @throws IllegalStateException
     * @author dank1234
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
     * Retrieves the [Item] that the [User] is currently holding.
     *
     * @return A nullable [Item] that is not handled.
     * @throws IllegalStateException
     * @throws NullPointerException
     * @author dank1234
     */
    fun getHeldItem(): Item? {
        val itemStack = getPlayer().inventory.itemInMainHand
        return if (itemStack.type == Material.AIR) null else Item.of(itemStack)
    }
    /**
     * Puts an [Item] in the [User]'s Inventory ([Menu]).
     * @param item The [Item] that will be put in the Inventory.
     *
     * @return [Result]
     * @throws IllegalStateException
     * @author dank1234
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
     * Adds a [Group] to the [User]'s groups.
     * @param group The [Group] that is being added.
     *
     * @return [Result]
     * @throws SQLException
     * @author dank1234
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
     * Removes a [Group] from the [User]'s groups.
     * @param group The [Group] that is being removed.
     *
     * @return [Result]
     * @throws SQLException
     * @author dank1234
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
     * Clears all of a [User]'s groups.
     *
     * @return [Result]
     * @throws SQLException
     * @author dank1234
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
     * Gets the group from the [User] wit the highest weight ([Int]).
     *
     * @return The [Group] object with the highest weight.
     * @throws NoSuchElementException
     * @author dank1234
     */
    fun getPrimaryGroup(): Group {
        return groups.maxByOrNull { it.weight }
            ?: throw NoSuchElementException("The user has no groups.")
    }

    /**
     * Adds a [Track] to the [User]'s tracks.
     * @param track The [Track] that is being added.
     *
     * @return [Result]
     * @throws SQLException
     * @author dank1234
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
     * Removes a [Track] from the [User]'s tracks.
     * @param track The [Track] that is being removed.
     *
     * @return [Result]
     * @throws SQLException
     * @author dank1234
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
     * Clears all of a [User]'s tracks.
     *
     * @return [Result]
     * @throws SQLException
     * @author dank1234
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
     * Adds a permission ([String]) to the [User]'s permissions.
     * @param permission The permission that is being added.
     *
     * @return [Result]
     * @throws SQLException
     * @author dank1234
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
     * Removes a permission ([String]) from the [User]'s permissions.
     * @param permission The permission that is being removed.
     *
     * @return [Result]
     * @throws SQLException
     * @author dank1234
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
     * Clears all of a [User]'s permissions.
     *
     * @return [Result]
     * @throws SQLException
     * @author dank1234
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
     * Sorts all of a [User]'s groups into weight order.
     *
     * @return [Result]
     * @author dank1234
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
     * Sorts all of a [User]'s groups into alphabetical order.
     *
     * @return [Result]
     * @author dank1234
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
     * Sorts all of a [User]'s permissions into alphabetical order.
     *
     * @return [Result]
     * @author dank1234
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
     * Retrieves the [User]'s [Colour] from the user_data [Database].
     *
     * @return Returns the [Colour] code.
     * @throws SQLException
     * @author dank1234
     */
    fun getChatColour(): String = this.chatColour
    /**
     * Updates the [User]'s chat colour.
     *
     * @return [Result]
     * @throws SQLException
     * @author dank1234
     */
    fun setChatColour(colour: String): Result {
        try {
            if (colour.length != 2) {
                return Result.FAILURE
            }
            if (colour.startsWith('&')) {
                Database.SQLUtils.executeUpdate("UPDATE user_data SET chat_colour = ? WHERE uuid = ?") { statement ->
                    statement.setString(1, colour)
                    statement.setString(2, uuid.toString())
                }
                this.chatColour = colour
                return Result.SUCCESSFUL
            }
            return Result.FAILURE
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }

    /**
     * Gets a [User]'s nickname.
     *
     * @return [String] The nickname.
     * @author dank1234
     */
    fun getNickname(): String = this.nickname
    /**
     * Updates the [User]'s current nickname.
     * @param nick [String] The new nickname.
     * @throws SQLException
     * @author dank1234
     */
    fun setNickname(nick: String): Result {
        try {
            if (nick.length <= 2) {
                return Result.BOUND_FAILURE
            }
            if (Regex("[^a-zA-Z0-9_]").containsMatchIn(nick)) {
                return Result.FAILURE
            }

            val rows = Database.SQLUtils.executeUpdate("UPDATE user_data SET nickname = ? WHERE uuid = ?") { statement ->
                statement.setString(1, nick)
                statement.setString(2, uuid.toString())
            }

            if (rows > 0) {
                this.nickname = nick
                return Result.SUCCESSFUL
            } else {
                return Result.FAILURE
            }
        }catch (e: Exception) {
            e.printStackTrace()
            return Result.EXCEPTION
        }
    }

    /**
     * Checks if the [User] is synced in the [Database].
     *
     * @return [Boolean]
     * @throws SQLException
     * @author dank1234
     */
    fun isSynced(): Boolean {
        try {
            return Database.SQLUtils.executeQuery(
                sql = "SELECT discord_id FROM users WHERE uuid = ?",
                consumer = { pstmt -> pstmt.setString(1, uuid.toString()) },
                mapper = { rs -> if (rs.next() && rs.getInt("discord_id") != 0) return@executeQuery true else false }
            ) ?: false
        }catch(e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    /**
     * Checks if the user currently has a sync code that has not been used.
     *
     * @return [Boolean]
     * @throws SQLException
     * @author dank1234
     */
    fun hasSyncCode(): Boolean {
        try {
            return Database.SQLUtils.executeQuery(
                sql = "SELECT 1 FROM sync_codes WHERE uuid = ?",
                consumer = { pstmt -> pstmt.setString(1, uuid.toString()) },
                mapper = { rs -> rs.next() }
            ) ?: false
        }catch(e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    /**
     * Gets the unused sync code from the [Database].
     *
     * @return The sync code.
     * @throws SQLException
     * @author dank1234
     */
    fun getSyncCode(): String {
        try {
            return Database.SQLUtils.executeQuery(
                sql = "SELECT 1 FROM sync_codes WHERE uuid = ?",
                consumer = { pstmt -> pstmt.setString(1, uuid.toString()) },
                mapper = { rs -> rs.getString("code") }
            ) ?: ""
        }catch(e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    /**
     * Retrieves the [User]'s Discord User ID from the database if they are synced.
     *
     * @return [String] The user's discord id.
     * @throws SQLException
     * @author dank1234
     */
    fun getDiscordID(): String {
        try {
            return Database.SQLUtils.executeQuery(
                "SELECT discord_id FROM users WHERE uuid = ?",
                { statement: PreparedStatement -> statement.setString(1, this.uuid.toString()) },
                { rs -> if (rs.next()) return@executeQuery rs.getString("discord_id") else "0" }
            ) ?: "0"
        }catch (e: Exception) {
            e.printStackTrace()
            return "0";
        }
    }

    /**
     * Retrieves the [Bukkit] form of the [User].
     *
     * @return A [Player] object. (The [Bukkit] version of a [User])
     * @throws IllegalStateException
     * @author dank1234
     */
    fun getPlayer(): Player {
        return Bukkit.getPlayer(uuid) ?: throw IllegalStateException("Player is not online. Cannot retrieve player.")
    }
    /**
     * Retrieves the [Bukkit] form of the [User].
     *
     * @return A [CommandSender] object. (The [Bukkit] version of a [User])
     * @throws IllegalStateException
     * @author dank1234
     */
    fun getCommandSender(): CommandSender {
        return Bukkit.getPlayer(uuid) ?: throw IllegalStateException("Player is not online. Cannot retrieve command sender.")
    }

    override fun toString(): String {
        return """
            User[
                uuid=$uuid, 
                username='$username', 
                gems=$gems, 
                tokens=$tokens, 
                souls=$souls, 
                groups=$groups, 
                tracks=$tracks, 
                permissions=$permissions
            ]
        """.trimMargin()
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (uuid != other.uuid) return false
        if (username != other.username) return false

        return true
    }
    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + username.hashCode()
        return result
    }
}