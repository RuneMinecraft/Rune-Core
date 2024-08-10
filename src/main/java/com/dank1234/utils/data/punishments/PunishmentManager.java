package com.dank1234.utils.data.punishments;

import com.dank1234.plugin.global.punishlite.Punishment;
import com.dank1234.plugin.global.punishlite.PunishmentType;
import com.dank1234.plugin.global.punishlite.modifiers.Active;
import com.dank1234.plugin.global.punishlite.modifiers.Public;
import com.dank1234.plugin.global.punishlite.modifiers.Silent;
import com.dank1234.utils.data.Database;
import com.dank1234.utils.players.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PunishmentManager {
    private static final String SCHEMA = "runemc";
    private static final String TABLE = "punishments";

    private static final Database database = Database.of(SCHEMA);

    public static void insert(Punishment punishment) {
        String insertSQL = "INSERT INTO " + TABLE + " (punishmentId, punishmentType, player, staff, reason, punishmentLength, startTime, endTime, modifier, active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
    public static Punishment getPunishment(String banId) {
        String query = "SELECT * FROM " + TABLE + " WHERE punishmentId = ?";
        try (PreparedStatement pstmt = database.connection().prepareStatement(query)) {
            pstmt.setString(1, banId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Punishment.of(
                        UUID.fromString(rs.getString("punishmentId")),
                        PunishmentType.valueOf(rs.getString("punishmentType")),
                        UUID.fromString(rs.getString("player")),
                        UUID.fromString(rs.getString("staff")),
                        rs.getString("reason"),
                        rs.getLong("punishmentLength"),
                        rs.getLong("startTime"),
                        rs.getLong("endTime"),
                        rs.getBoolean("silent") ? Silent.class : Public.class,
                        rs.getBoolean("active") ? Active.class : null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Punishment[] getPunishments(User user) {
        List<Punishment> punishments = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE + " WHERE player = ?";
        try (PreparedStatement pstmt = database.connection().prepareStatement(query)) {
            pstmt.setString(1, user.uuid().toString());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Punishment punishment = Punishment.of(
                        UUID.fromString(rs.getString("punishmentId")),
                        PunishmentType.valueOf(rs.getString("punishmentType")),
                        UUID.fromString(rs.getString("player")),
                        UUID.fromString(rs.getString("staff")),
                        rs.getString("reason"),
                        rs.getLong("punishmentLength"),
                        rs.getLong("startTime"),
                        rs.getLong("endTime"),
                        rs.getBoolean("silent") ? Silent.class : Public.class,
                        rs.getBoolean("active") ? Active.class : null
                );
                punishments.add(punishment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return punishments.toArray(new Punishment[0]);
    }
    public static Punishment[] getPunishments(PunishmentType type) {
        List<Punishment> punishments = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE + " WHERE punishmentType = ?";
        try (PreparedStatement pstmt = database.connection().prepareStatement(query)) {
            pstmt.setString(1, type.name());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Punishment punishment = Punishment.of(
                        UUID.fromString(rs.getString("punishmentId")),
                        PunishmentType.valueOf(rs.getString("punishmentType")),
                        UUID.fromString(rs.getString("player")),
                        UUID.fromString(rs.getString("staff")),
                        rs.getString("reason"),
                        rs.getLong("punishmentLength"),
                        rs.getLong("startTime"),
                        rs.getLong("endTime"),
                        rs.getBoolean("silent") ? Silent.class : Public.class,
                        rs.getBoolean("active") ? Active.class : null
                );
                punishments.add(punishment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return punishments.toArray(new Punishment[0]);
    }
}
