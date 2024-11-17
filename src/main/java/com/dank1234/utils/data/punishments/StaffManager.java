package com.dank1234.utils.data.punishments;

import com.dank1234.utils.data.Database;
import com.dank1234.utils.wrapper.player.StaffTrack;
import com.dank1234.utils.wrapper.player.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StaffManager {
    private static final String SCHEMA = "runemc";
    private static final String TABLE = "staff";
    private static final Database database = Database.of(SCHEMA);

    public static void insert(User user, StaffTrack rank) {
        String insertSQL = "INSERT INTO " + TABLE + " (uuid, rank) VALUES (?, ?)";
        try (PreparedStatement pstmt = database.connection().prepareStatement(insertSQL)) {
            pstmt.setString(1, user.uuid().toString());
            pstmt.setString(2, rank.name());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static StaffTrack getRank(User user) {
        String selectSQL = "SELECT rank FROM " + TABLE + " WHERE uuid = ?";
        try (PreparedStatement pstmt = database.connection().prepareStatement(selectSQL)) {
            pstmt.setString(1, user.uuid().toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return StaffTrack.valueOf(rs.getString("rank"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setRank(User user, StaffTrack newRank) {
        String updateSQL = "UPDATE " + TABLE + " SET rank = ? WHERE uuid = ?";
        try (PreparedStatement pstmt = database.connection().prepareStatement(updateSQL)) {
            pstmt.setString(1, newRank.name());
            pstmt.setString(2, user.uuid().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<User> getAllStaff(StaffTrack rank) {
        List<User> staffList = new ArrayList<>();
        String selectSQL = "SELECT uuid FROM " + TABLE + " WHERE rank = ?";
        try (PreparedStatement pstmt = database.connection().prepareStatement(selectSQL)) {
            pstmt.setString(1, rank.name());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    UUID uuid = UUID.fromString(rs.getString("uuid"));
                    staffList.add(User.of(uuid));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    public static void removeStaff(User user) {
        String deleteSQL = "DELETE FROM " + TABLE + " WHERE uuid = ?";
        try (PreparedStatement pstmt = database.connection().prepareStatement(deleteSQL)) {
            pstmt.setString(1, user.uuid().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
