package com.dank1234.plugin.global.ranks

import com.dank1234.utils.data.Database

import java.sql.ResultSet
import java.sql.SQLException

data class Track(
    var id: Int? = null,
    var name: String,
    val groups: MutableList<Group> = mutableListOf()
) {
    companion object {
        @Throws(SQLException::class)
        fun create(name: String): Track {
            val sql = "INSERT INTO tracks (name) VALUES (?)"
            val id = Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setString(1, name)
            }
            return Track(id, name)
        }

        @Throws(SQLException::class)
        fun get(name: String): Track? {
            val sql = "SELECT * FROM tracks WHERE name = ?"
            return Database.SQLUtils.executeQuery(sql, { pstmt ->
                pstmt.setString(1, name)
            }) { rs ->
                if (rs.next()) mapResultSetToTrack(rs) else null
            }
        }

        @Throws(SQLException::class)
        fun getAllTracks(): List<Track> {
            val sql = "SELECT * FROM tracks"
            return Database.SQLUtils.executeQuery(sql, {}, { rs ->
                val tracks = mutableListOf<Track>()
                while (rs.next()) {
                    tracks.add(mapResultSetToTrack(rs))
                }
                tracks
            }) ?: emptyList()
        }

        private fun mapResultSetToTrack(rs: ResultSet): Track {
            val id = rs.getInt("id")
            val name = rs.getString("name")
            val track = Track(id, name)

            // Fetch associated groups
            val groupSql = "SELECT g.name FROM track_groups tg INNER JOIN groups g ON tg.group_id = g.id WHERE tg.track_id = ?"
            Database.SQLUtils.executeQuery(groupSql, { pstmt ->
                pstmt.setInt(1, id)
            }) { groupRs ->
                while (groupRs.next()) {
                    val groupName = groupRs.getString("name")
                    val group = Group.get(groupName)
                    if (group != null) {
                        track.groups.add(group)
                    }
                }
            }
            return track
        }

        @JvmStatic
        @Throws(SQLException::class)
        fun ensureTables() {
            Database.SQLUtils.executeUpdate("""
                CREATE TABLE IF NOT EXISTS tracks (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) UNIQUE NOT NULL
                );
            """.trimIndent())

            Database.SQLUtils.executeUpdate("""
                CREATE TABLE IF NOT EXISTS track_groups (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    track_id INT NOT NULL,
                    group_id INT NOT NULL,
                    FOREIGN KEY (track_id) REFERENCES tracks(id) ON DELETE CASCADE,
                    FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE CASCADE
                );
            """.trimIndent())
        }
    }

    @Throws(SQLException::class)
    fun addGroup(groupName: String) {
        val group = Group.get(groupName) ?: return
        if (!groups.contains(group)) {
            val sql = "INSERT INTO track_groups (track_id, group_id) VALUES (?, ?)"
            Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setInt(1, id!!)
                pstmt.setInt(2, group.id!!)
            }
            groups.add(group)
        }
    }

    @Throws(SQLException::class)
    fun removeGroup(groupName: String) {
        val group = groups.find { it.name.equals(groupName, ignoreCase = true) }
        if (group != null) {
            val sql = "DELETE FROM track_groups WHERE track_id = ? AND group_id = ?"
            Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setInt(1, id!!)
                pstmt.setInt(2, group.id!!)
            }
            groups.remove(group)
        }
    }
}
