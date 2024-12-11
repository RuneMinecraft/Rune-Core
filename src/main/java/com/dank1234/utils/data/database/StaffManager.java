package com.dank1234.utils.data.database;

import com.dank1234.utils.wrapper.player.staff.StaffRank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class StaffManager extends SQLUtils{
    private static final String TABLE = "staff";

    public static void ensureTableExists() {
        String sql = "CREATE TABLE IF NOT EXISTS staff (" +
                "uuid VARCHAR(36) PRIMARY KEY, " +
                "rank VARCHAR(50) NOT NULL, " +
                "time BIGINT DEFAULT 0, " +
                "messages INT DEFAULT 0, " +
                "warns INT DEFAULT 0, " +
                "mutes INT DEFAULT 0, " +
                "bans INT DEFAULT 0, " +
                "staffmode BOOLEAN DEFAULT FALSE, " +
                "FOREIGN KEY (uuid) REFERENCES users(uuid) ON DELETE CASCADE" +
                ")";
        executeUpdate(sql);
    }

    public static void insert(Staff user) {
        String sql = "INSERT INTO " + TABLE + " (uuid, rank, time, messages, warns, mutes, bans, staffmode) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        executeUpdate(sql, pstmt -> {
            pstmt.setString(1, user.getUuid().toString());
            pstmt.setString(2, user.rank().name());
            pstmt.setLong(3, user.time());
            pstmt.setInt(4, user.messages());
            pstmt.setInt(5, user.warns());
            pstmt.setInt(6, user.mutes());
            pstmt.setInt(7, user.bans());
            pstmt.setBoolean(8, user.staffMode());
        });
    }
    public static void delete(UUID uuid) {
        String sql = "DELETE FROM " + TABLE + " WHERE uuid = ?";
        executeUpdate(sql, pstmt -> pstmt.setString(1, uuid.toString()));
    }

    public static Optional<Object> getValue(UUID uuid, String field) {
        String sql = "SELECT " + field + " FROM staff WHERE uuid = ?";
        return executeQuery(sql, pstmt -> pstmt.setString(1, uuid.toString()), rs -> rs.getObject(field));
    }
    public static boolean setValue(UUID uuid, String field, Object value) {
        String sql = "UPDATE staff SET " + field + " = ? WHERE uuid = ?";
        return executeUpdate(sql, pstmt -> {
            pstmt.setObject(1, value);
            pstmt.setString(2, uuid.toString());
        }) > 0;
    }

    public static Optional<List<Staff>> getAll(StaffRank rank) {
        String sql = "SELECT uuid FROM staff WHERE rank = ?";
        return executeQuery(sql, pstmt -> pstmt.setString(1, rank.name()), rs -> {
            List<Staff> staffList = new ArrayList<>();
            while (rs.next()) {
                staffList.add(mapResultSetToUser(rs));
            }
            return staffList;
        });
    }
    public static Optional<List<Staff>> getAll() {
        String sql = "SELECT uuid FROM staff";
        return executeQuery(sql, pstmt -> {}, rs -> {
            List<Staff> staffList = new ArrayList<>();
            while (rs.next()) {
                staffList.add(mapResultSetToUser(rs));
            }
            return staffList;
        });
    }

    public static Optional<Staff> getStaff(UUID uuid) {
        String sql = "SELECT * FROM staff WHERE uuid = ?";
        return executeQuery(sql, pstmt -> pstmt.setString(1, uuid.toString()), rs -> rs.next() ? mapResultSetToUser(rs) : null);
    }
    public static Optional<Staff> getStaff(String username) {
        String sql = "SELECT staff.* FROM staff " +
                "INNER JOIN users ON staff.uuid = users.uuid " +
                "WHERE users.username = ?";
        return executeQuery(sql, pstmt -> pstmt.setString(1, username), rs -> rs.next() ? mapResultSetToUser(rs) : null);
    }
    private static Staff mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = User.of(UUID.fromString(rs.getString("uuid")));
        String rankString = rs.getString("rank");
        StaffRank rank;

        try {
            rank = StaffRank.valueOf(rankString);
        } catch (IllegalArgumentException | NullPointerException e) {
            rank = StaffRank.HELPER;
        }

        return Staff.Companion.of(user.getUuid(), user.getUsername(), rank)
                .setTime(rs.getLong("time"))
                .setMessages(rs.getInt("messages"))
                .setWarns(rs.getInt("warns"))
                .setMutes(rs.getInt("mutes"))
                .setBans(rs.getInt("bans"))
                .setStaffMode(rs.getBoolean("staffmode"));
    }
}