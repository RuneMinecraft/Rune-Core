package com.dank1234.plugin.global.ranks

import com.dank1234.utils.data.Database

import java.sql.ResultSet
import java.sql.SQLException

data class Group(
    var id: Int? = null,
    var name: String,
    var prefix: String,
    var suffix: String? = null,
    var weight: Int,
    val inheritedGroups: MutableList<Group> = mutableListOf(),
    val permissions: MutableList<String> = mutableListOf()
) {
    companion object {
        @Throws(SQLException::class)
        fun create(
            name: String,
            prefix: String,
            suffix: String? = null,
            weight: Int
        ): Group {
            val sql = "INSERT INTO groups (name, prefix, suffix, weight) VALUES (?, ?, ?, ?)"
            val id = Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setString(1, name)
                pstmt.setString(2, prefix)
                pstmt.setString(3, suffix)
                pstmt.setInt(4, weight)
            }
            return Group(id, name, prefix, suffix, weight)
        }

        @Throws(SQLException::class)
        fun get(name: String): Group? {
            val sql = "SELECT * FROM groups WHERE name = ?"
            return Database.SQLUtils.executeQuery(sql, { pstmt ->
                pstmt.setString(1, name)
            }) { rs ->
                if (rs.next()) mapResultSetToGroup(rs) else null
            }
        }

        @Throws(SQLException::class)
        fun getAllGroups(): List<Group> {
            val sql = "SELECT * FROM groups"
            return Database.SQLUtils.executeQuery(sql, {}, { rs ->
                val groups = mutableListOf<Group>()
                while (rs.next()) {
                    groups.add(mapResultSetToGroup(rs))
                }
                groups
            }) ?: emptyList()
        }

        private fun mapResultSetToGroup(rs: ResultSet): Group {
            val id = rs.getInt("id")
            val name = rs.getString("name")
            val prefix = rs.getString("prefix")
            val suffix = rs.getString("suffix")
            val weight = rs.getInt("weight")
            val group = Group(id, name, prefix, suffix, weight)

            val inheritedSql = "SELECT g2.name FROM group_inheritances gi INNER JOIN groups g2 ON gi.inherited_group_id = g2.id WHERE gi.group_id = ?"
            Database.SQLUtils.executeQuery(inheritedSql, { pstmt ->
                pstmt.setInt(1, id)
            }) { inheritedRs ->
                while (inheritedRs.next()) {
                    val inheritedGroup = get(inheritedRs.getString("name"))
                    if (inheritedGroup != null) {
                        group.inheritedGroups.add(inheritedGroup)
                    }
                }
            }

            val permissionsSql = "SELECT permission FROM group_permissions WHERE group_id = ?"
            Database.SQLUtils.executeQuery(permissionsSql, { pstmt ->
                pstmt.setInt(1, id)
            }) { permsRs ->
                while (permsRs.next()) {
                    group.permissions.add(permsRs.getString("permission"))
                }
            }

            return group
        }

        @JvmStatic
        @Throws(SQLException::class)
        fun ensureTables() {
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
                CREATE TABLE IF NOT EXISTS group_permissions (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    group_id INT NOT NULL,
                    permission VARCHAR(255) NOT NULL,
                    FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE CASCADE
                );
            """.trimIndent())

            Database.SQLUtils.executeUpdate("""
                CREATE TABLE IF NOT EXISTS group_inheritances (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    group_id INT NOT NULL,
                    inherited_group_id INT NOT NULL,
                    FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE CASCADE,
                    FOREIGN KEY (inherited_group_id) REFERENCES groups(id) ON DELETE CASCADE
                );
            """.trimIndent())
        }
    }

    @Throws(SQLException::class)
    fun addPermission(permission: String) {
        if (!permissions.contains(permission)) {
            val sql = "INSERT INTO group_permissions (group_id, permission) VALUES (?, ?)"
            Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setInt(1, id!!)
                pstmt.setString(2, permission)
            }
            permissions.add(permission)
        }
    }

    @Throws(SQLException::class)
    fun removePermission(permission: String) {
        if (permissions.remove(permission)) {
            val sql = "DELETE FROM group_permissions WHERE group_id = ? AND permission = ?"
            Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setInt(1, id!!)
                pstmt.setString(2, permission)
            }
        }
    }

    @Throws(SQLException::class)
    fun addInheritedGroup(groupName: String) {
        val inherited = get(groupName) ?: return
        if (!inheritedGroups.contains(inherited)) {
            val sql = "INSERT INTO group_inheritances (group_id, inherited_group_id) VALUES (?, ?)"
            Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setInt(1, id!!)
                pstmt.setInt(2, inherited.id!!)
            }
            inheritedGroups.add(inherited)
        }
    }

    @Throws(SQLException::class)
    fun removeInheritedGroup(groupName: String) {
        val inherited = inheritedGroups.find { it.name.equals(groupName, ignoreCase = true) }
        if (inherited != null) {
            val sql = "DELETE FROM group_inheritances WHERE group_id = ? AND inherited_group_id = ?"
            Database.SQLUtils.executeUpdate(sql) { pstmt ->
                pstmt.setInt(1, id!!)
                pstmt.setInt(2, inherited.id!!)
            }
            inheritedGroups.remove(inherited)
        }
    }
}