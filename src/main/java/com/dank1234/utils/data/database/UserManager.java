package com.dank1234.utils.data.database;

import com.dank1234.plugin.Main;
import com.dank1234.utils.data.Database;
import com.dank1234.utils.wrapper.player.User;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserManager {
    private static final Database database = Main.get().database();
    private static final String TABLE = "users";

    public static void ensureTableExists() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
                "uuid CHAR(36) PRIMARY KEY, " +
                "username VARCHAR(255) NOT NULL" +
                ")";
        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insert(User user) {
        String sql = "INSERT INTO " + TABLE + " (uuid, username) VALUES (?, ?)";
        try (Connection conn = database.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.uuid().toString());
            pstmt.setString(2, user.username());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertBatch(List<User> users) {
        String sql = "INSERT INTO " + TABLE + " (uuid, username) VALUES (?, ?)";
        try (Connection conn = database.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (User user : users) {
                pstmt.setString(1, user.uuid().toString());
                pstmt.setString(2, user.username());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Optional<User> getUser(UUID uuid) {
        String sql = "SELECT * FROM " + TABLE + " WHERE uuid = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(User.of(
                            UUID.fromString(rs.getString("uuid")),
                            rs.getString("username")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<User> getUser(String name) {
        String sql = "SELECT * FROM " + TABLE + " WHERE username = ?";
        try (Connection conn = database.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(User.of(
                            UUID.fromString(rs.getString("uuid")),
                            rs.getString("username")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
