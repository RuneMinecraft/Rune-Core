package com.dank1234.utils.wrapper.player.staff;

import com.dank1234.plugin.Codex
import com.dank1234.plugin.global.ranks.Group
import com.dank1234.plugin.global.ranks.Ranks
import com.dank1234.plugin.global.ranks.Track
import com.dank1234.utils.data.Database
import com.dank1234.utils.wrapper.player.User;
import java.sql.ResultSet
import java.util.*

open class Staff(
    uuid: UUID,
    private var staffRank: Ranks,
    private var onlineTime: Long = 0,
    private var messagesSent: Int = 0,
    private var warns: Int = 0,
    private var mutes: Int = 0,
    private var bans: Int = 0,
    var staffMode: Boolean = false,
    groups: MutableList<Group> = mutableListOf(),
    tracks: MutableMap<Track, Int> = mutableMapOf(),
    permissions: MutableList<String> = mutableListOf()
) : User(uuid, of(uuid).username, groups = groups, tracks = tracks, permissions = permissions) {

    companion object {
        @JvmStatic fun of(uuid: UUID, staffRank: Ranks): Staff {
            return getStaff(uuid).orElse(Staff(uuid, staffRank).apply {
                Database.SQLUtils.executeUpdate("INSERT INTO staff (uuid, staff_rank) VALUES (?, ?);") { pstmt ->
                    pstmt.setString(1, uuid.toString())
                    pstmt.setString(2, staffRank.name)
                }
                Codex.addUser(this)
            })
        }
        @JvmStatic fun getStaff(uuid: UUID): Optional<Staff> {
            val sql = "SELECT * FROM staff WHERE uuid = ?"
            return Database.SQLUtils.executeQuery(sql, { pstmt -> pstmt.setString(1, uuid.toString()) }) { rs ->
                if (rs.next()) mapResultSet(rs) else null
            }?.let {
                Optional.of(it)
            } ?: Optional.empty()
        }
        @JvmStatic fun exists(uuid: UUID): Boolean {
            val sql = "SELECT 1 FROM staff WHERE uuid = ? LIMIT 1"
            return Database.SQLUtils.executeQuery(sql, { pstmt -> pstmt.setString(1, uuid.toString()) }) { rs -> rs.next() } == true
        }

        @JvmStatic fun ensureTables() {
            Database.SQLUtils.executeUpdate("""
                CREATE TABLE IF NOT EXISTS staff (
                    uuid            VARCHAR(36)     PRIMARY KEY NOT NULL,
                    staff_rank      VARCHAR(255)    NOT NULL,
                    online_time     LONG            DEFAULT 0,
                    messages_sent   INT             DEFAULT 0,
                    warns           INT             DEFAULT 0,
                    mutes           INT             DEFAULT 0,
                    bans            INT             DEFAULT 0,
                    staffmode       BOOLEAN         DEFAULT FALSE,
                    FOREIGN KEY (uuid) REFERENCES users(uuid) ON DELETE CASCADE
                );
            """.trimIndent())
        }

        private fun mapResultSet(rs: ResultSet): Staff {
            val uuid = UUID.fromString(rs.getString("uuid"))
            val staffRank = Ranks.valueOf(rs.getString("staff_rank"))
            val onlineTime = rs.getLong("online_time")
            val messagesSent = rs.getInt("messages_sent")
            val warns = rs.getInt("warns")
            val mutes = rs.getInt("mutes")
            val bans = rs.getInt("bans")
            val staffMode = rs.getBoolean("staffMode")

            return Staff(uuid, staffRank, onlineTime = onlineTime, messagesSent = messagesSent, warns = warns, mutes = mutes, bans = bans, staffMode = staffMode)
        }
    }

    fun promote() {
        // +1 staff rank
    }
    fun demote() {
        // -1 staff rank
    }

    fun staffMode(staffMode: Boolean): Staff {
        if (this.staffMode == staffMode) {
            return this
        }
        this.staffMode = staffMode

        Database.SQLUtils.executeUpdate("UPDATE staff SET staffMode = ? WHERE uuid = ?") { statement ->
            statement.setBoolean(1, staffMode)
            statement.setString(2, uuid.toString())
        }

        synchronized(this) {
            val user = of(this.uuid)
            if (staffMode) {
                Database.SQLUtils.executeQuery("SELECT staff_rank FROM staff WHERE uuid = ?", { pstmt ->
                    pstmt.setString(1, uuid.toString())
                }) { rs ->
                    if (rs.next())  rs.getString("staff_rank") else null
                }?.let {
                    user.addGroup(Ranks.valueOf(it).group())
                }
            }else {
                for (group in Ranks.entries) {
                    if (user.groups.contains(element = group.group()) && group.rankType() == Ranks.RankType.STAFF) {
                        user.groups.remove(group.group())
                    }
                }
            }
        }
        return this
    }
    fun toggleStaffMode() {
        staffMode(!staffMode)
    }

    fun incrementMessages() {
        messagesSent++
    }
    fun incrementWarns() {
        warns++
    }
    fun incrementMutes() {
        mutes++
    }
    fun incrementBans() {
        bans++
    }

    fun incrementTime(time: Long) {
        onlineTime += time
    }
}