package com.dank1234.utils.data.punishments;

import com.dank1234.plugin.global.punishlite.Punishment;
import com.dank1234.plugin.global.punishlite.PunishmentType;
import com.dank1234.plugin.global.punishlite.modifiers.Silent;
import com.dank1234.utils.data.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;

public class PunishmentManager {
    private static final String SCHEMA = "runemc";
    private static final String TABLE = "punishments";

    private static final Database database = Database.of(SCHEMA, TABLE);

    public static void insert(Punishment punishment) {
        String insertSQL = "INSERT INTO " + TABLE + " (punishmentId, type, target, staff, reason, punishmentLength, startTime, endTime, modifier, active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = database.connection().prepareStatement(insertSQL)) {
            pstmt.setString(1, punishment.punishmentId().toString());
            pstmt.setString(2, punishment.type().name());
            pstmt.setString(3, punishment.target().toString());
            pstmt.setString(4, punishment.staff().toString());
            pstmt.setString(5, punishment.reason());
            pstmt.setLong(6, punishment.punishmentLength());
            pstmt.setLong(7, punishment.startTime());
            pstmt.setLong(8, punishment.endTime());
            pstmt.setBoolean(9, Arrays.stream(punishment.modifiers()).toList().contains(Silent.class));
            pstmt.setBoolean(10, punishment.active());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Punishment getPunishment(String banId) {
        String query = "SELECT * FROM " + TABLE + " WHERE banId = ?";
        try (PreparedStatement pstmt = database.connection().prepareStatement(query)) {
            pstmt.setString(1, banId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Punishment.of(
                        UUID.fromString(rs.getString("banId")),
                        PunishmentType.valueOf(rs.getString("type")),
                        UUID.fromString(rs.getString("player")),
                        UUID.fromString(rs.getString("punisher")),
                        rs.getString("reason"),
                        rs.getLong("length"),
                        rs.getLong("endTime"),
                        rs.getBoolean("silent"),
                        rs.getBoolean("active")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
