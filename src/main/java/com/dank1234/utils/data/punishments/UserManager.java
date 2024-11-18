package com.dank1234.utils.data.punishments;

import com.dank1234.plugin.Bootstrap;
import com.dank1234.plugin.Main;
import com.dank1234.utils.data.Database;
import com.dank1234.utils.wrapper.player.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class UserManager {
    private static final String TABLE = "users";

    private static final Database database = Main.get().database();

    public static void insert(User user) {
        String insertSQL = "INSERT INTO " + TABLE + " (uuid, username) VALUES (?, ?)";
        try (Connection conn = database.getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, user.uuid().toString());
            pstmt.setString(2, user.username());
            pstmt.executeUpdate();            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertBatch(List<User> users) {
        String insertSQL = "INSERT INTO " + TABLE + " (uuid, username) VALUES (?, ?)";
        try (Connection conn = database.getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
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

    public static User getUser(UUID uuid) {
        String query = "SELECT * FROM " + TABLE + " WHERE uuid = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return User.of(
                        UUID.fromString(rs.getString("uuid")),
                        rs.getString("username")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getUser(String name) {
        String query = "SELECT * FROM " + TABLE + " WHERE username = ?";
        try (Connection conn = database.getConnection(); 
         PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return User.of(
                        UUID.fromString(rs.getString("uuid")),
                        rs.getString("username")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
