package com.dank1234.utils.data.database;

import com.dank1234.plugin.Main;
import com.dank1234.utils.data.Database;
import com.dank1234.utils.wrapper.player.User;
import com.dank1234.utils.wrapper.player.staff.Staff;
import com.dank1234.utils.wrapper.player.staff.StaffRank;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class StaffManager {
    private static final Database database = Main.get().database();
    private static final String TABLE = "staff";
    private static Connection connection;

    static {
        try {
            connection = database.getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void ensureTableExists() {
        String sql = "CREATE TABLE IF NOT EXISTS staff (" +
                "uuid CHAR(36) PRIMARY KEY, " +
                "rank VARCHAR(50) NOT NULL, " +
                "time BIGINT DEFAULT 0, " +
                "messages INT DEFAULT 0, " +
                "warns INT DEFAULT 0, " +
                "mutes INT DEFAULT 0, " +
                "bans INT DEFAULT 0, " +
                "staffmode BOOLEAN DEFAULT FALSE, " +
                "FOREIGN KEY (uuid) REFERENCES users(uuid) ON DELETE CASCADE" +
                ")";
        try (Connection conn = connection;
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insert(Staff user) {
        String sql = "INSERT INTO " + TABLE + " (uuid, rank, time, messages, warns, mutes, bans, staffmode) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.uuid().toString());
            pstmt.setString(2, user.rank());
            pstmt.setLong(3, user.time());
            pstmt.setInt(4, user.messages());
            pstmt.setInt(5, user.warns());
            pstmt.setInt(6, user.mutes());
            pstmt.setInt(7, user.bans());
            pstmt.setBoolean(8, user.isInStaffMode());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Optional<Object> getValue(UUID uuid, String field) {
        String sql = "SELECT " + field + " FROM staff WHERE uuid = ?";
        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.ofNullable(rs.getObject(field));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static boolean setValue(UUID uuid, String field, Object value) {
        String sql = "UPDATE staff SET " + field + " = ? WHERE uuid = ?";
        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, value);
            pstmt.setString(2, uuid.toString());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Optional<List<Staff>> getAll(StaffRank rank) {
        String sql = "SELECT uuid FROM staff WHERE rank = ?";
        List<Staff> users = new ArrayList<>();

        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rank.toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(Staff.of(rs.getString("uuid")));
            }
            return Optional.of(users);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<Staff> getStaff(UUID uuid) {
        String sql = "SELECT * FROM staff WHERE uuid = ?";
        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<Staff> getStaff(String username) {
        String sql = "SELECT staff.* FROM staff " +
                "INNER JOIN users ON staff.uuid = users.uuid " +
                "WHERE users.username = ?";
        try (Connection conn = connection;
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static boolean isInStaffMode(UUID uuid) {
        return getValue(uuid, "staffmode").map(value -> (Boolean) value).orElse(false);
    }

    public static boolean setStaffMode(UUID uuid, boolean staffMode) {
        return setValue(uuid, "staffmode", staffMode);
    }

    private static Staff mapResultSetToUser(ResultSet rs) throws SQLException {
        Staff user = Staff.of(rs.getString("uuid"));
        user.setRank(rs.getString("rank"));
        user.setTime(rs.getLong("time"));
        user.setMessages(rs.getInt("messages"));
        user.setWarns(rs.getInt("warns"));
        user.setMutes(rs.getInt("mutes"));
        user.setBans(rs.getInt("bans"));
        user.setInStaffMode(rs.getBoolean("staffmode"));
        return user;
    }
}