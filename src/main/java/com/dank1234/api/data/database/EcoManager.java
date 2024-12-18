package com.dank1234.api.data.database;

import com.dank1234.api.wrapper.player.User;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class EcoManager extends SQLUtils {
    private static final String TABLE = "economy";

    public static void ensureTableExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS economy (
                        uuid       VARCHAR(36)   PRIMARY KEY,
                        tokens     FLOAT(53)     DEFAULT 0,
                        souls      FLOAT(53)     DEFAULT 0,
                        gems       FLOAT(53)     DEFAULT 0,
                        FOREIGN KEY (uuid) REFERENCES users(uuid) ON DELETE CASCADE
                )
                """;
        executeUpdate(sql);
    }
    public static void insert(User user) {
        String sql = "INSERT INTO economy (uuid) VALUES (?);";

        executeUpdate(sql, pstmt -> {
            pstmt.setString(1, user.getUuid().toString());
        });
    }

    public static Optional<Double> getValue(UUID uuid, String field) {
        if ("uuid".equals(field)) return Optional.of(0.0);

        String sql = "SELECT " + field + " FROM " + TABLE + " WHERE uuid = ?;";
        return executeQuery(sql, pstmt -> pstmt.setString(1, uuid.toString()), rs -> {
            if (rs.next()) return rs.getDouble(field);
            return null;
        }).map(Optional::ofNullable).orElse(Optional.empty());
    }
    public static boolean setValue(UUID uuid, String field, Double value) {
        if (Objects.equals(field, "uuid")) {
            return false;
        }
        String sql = "UPDATE "+TABLE+" SET " + field + " = ? WHERE uuid = ?;";
        return executeUpdate(sql, pstmt -> {
            pstmt.setObject(1, value);
            pstmt.setString(2, uuid.toString());
        }) > 0;
    }
}
