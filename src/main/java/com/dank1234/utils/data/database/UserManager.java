package com.dank1234.utils.data.database;

import com.dank1234.utils.data.Database;
import com.dank1234.utils.wrapper.player.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserManager extends SQLUtils {
    private static final String TABLE = "users";

    public static void insert(User user) {
        String sql = "INSERT INTO " + TABLE + " (uuid, username) VALUES (?, ?)";
        executeUpdate(sql, pstmt -> {
            pstmt.setString(1, user.getUuid().toString());
            pstmt.setString(2, user.getUsername());
        });
    }

    public static void insertBatch(List<User> users) {
        String sql = "INSERT INTO " + TABLE + " (uuid, username) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (User user : users) {
                pstmt.setString(1, user.getUuid().toString());
                pstmt.setString(2, user.getUsername());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Optional<User> getUser(UUID uuid) {
        String sql = "SELECT * FROM " + TABLE + " WHERE uuid = ?";
        return executeQuery(sql, pstmt -> pstmt.setString(1, uuid.toString()), UserManager::mapResultSetToUser);
    }

    public static Optional<User> getUser(String username) {
        String sql = "SELECT * FROM " + TABLE + " WHERE username = ?";
        return executeQuery(sql, pstmt -> pstmt.setString(1, username), UserManager::mapResultSetToUser);
    }

    private static User mapResultSetToUser(ResultSet rs) throws SQLException {
        return User.of(UUID.fromString(rs.getString("uuid")), rs.getString("username"));
    }

    public static boolean exists(UUID uuid) {
        String sql = "SELECT 1 FROM " + TABLE + " WHERE uuid = ? LIMIT 1";
        return executeQuery(sql, pstmt -> pstmt.setString(1, uuid.toString()), ResultSet::next).get();
    }

    public static boolean exists(String name) {
        String sql = "SELECT 1 FROM " + TABLE + " WHERE username = ? LIMIT 1";
        return executeQuery(sql, pstmt -> pstmt.setString(1, name), ResultSet::next).get();
    }
}
