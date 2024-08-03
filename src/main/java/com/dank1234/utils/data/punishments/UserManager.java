package com.dank1234.utils.data.punishments;

import com.dank1234.plugin.global.punishlite.players.User;
import com.dank1234.utils.data.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserManager {
    private static final String SCHEMA = "runemc";
    private static final String TABLE = "users";

    private static final Database database = Database.of(SCHEMA, TABLE);

    public static void insert(User user) {
        String insertSQL = "INSERT INTO " + TABLE + " (uuid, username) VALUES (?, ?)";
        try (PreparedStatement pstmt = database.connection().prepareStatement(insertSQL)) {
            pstmt.setString(1, user.uuid().toString());
            pstmt.setString(2, user.username());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User getUser(UUID uuid) {
        String query = "SELECT * FROM " + TABLE + " WHERE uuid = ?";
        try (PreparedStatement pstmt = database.connection().prepareStatement(query)) {
            pstmt.setString(1, String.valueOf(uuid));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return User.of(
                        UUID.fromString(rs.getString("uuid")),
                        rs.getString("username")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static User getUser(String name) {
        String query = "SELECT * FROM " + TABLE + " WHERE username = ?";
        try (PreparedStatement pstmt = database.connection().prepareStatement(query)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return User.of(
                        UUID.fromString(rs.getString("uuid")),
                        rs.getString("username")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
