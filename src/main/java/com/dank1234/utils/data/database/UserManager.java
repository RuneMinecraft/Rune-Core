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

public class UserManager {
    private static final String TABLE = "users";

    public static void ensureTableExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS %s (
                    uuid CHAR(36) PRIMARY KEY,
                    username VARCHAR(255) NOT NULL
                )
                """.formatted(TABLE);
        executeUpdate(sql);
    }

    public static void insert(User user) {
        String sql = "INSERT INTO " + TABLE + " (uuid, username) VALUES (?, ?)";
        executeUpdate(sql, pstmt -> {
            pstmt.setString(1, user.uuid().toString());
            pstmt.setString(2, user.username());
        });
    }

    public static void insertBatch(List<User> users) {
        String sql = "INSERT INTO " + TABLE + " (uuid, username) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
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
        return executeQuery(sql, pstmt -> pstmt.setString(1, String.valueOf(uuid)), rs -> rs.next() ? mapResultSetToUser(rs) : null);
    }

    public static Optional<User> getUser(String name) {
        String sql = "SELECT * FROM " + TABLE + " WHERE username = ?";
        return executeQuery(sql, pstmt -> pstmt.setString(1, name), rs -> rs.next() ? mapResultSetToUser(rs) : null);
    }

    private static int executeUpdate(String sql) {
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Error executing update: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    private static int executeUpdate(String sql, SQLConsumer<PreparedStatement> consumer) {
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            consumer.accept(pstmt);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error executing update with parameters: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    private static <T> Optional<T> executeQuery(String sql, SQLConsumer<PreparedStatement> consumer, SQLFunction<ResultSet, T> mapper) {
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            consumer.accept(pstmt);
            try (ResultSet rs = pstmt.executeQuery()) {
                return Optional.ofNullable(mapper.apply(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private static User mapResultSetToUser(ResultSet rs) throws SQLException {
        return User.of(UUID.fromString(rs.getString("uuid")), rs.getString("username"));
    }

    @FunctionalInterface
    private interface SQLConsumer<T> {
        void accept(T t) throws SQLException;
    }

    @FunctionalInterface
    private interface SQLFunction<T, R> {
        R apply(T t) throws SQLException;
    }
}
