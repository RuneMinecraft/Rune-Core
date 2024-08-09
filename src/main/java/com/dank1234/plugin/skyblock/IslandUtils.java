package com.dank1234.plugin.skyblock;

import com.dank1234.plugin.Main;
import com.dank1234.utils.data.Database;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class IslandUtils {
    private static final Database database = Main.get().database();
    private static final Connection connection = database.connection();

    public static void createTables() throws SQLException {
        String createIslandTable = """
            CREATE TABLE Island (
                islandId UUID PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                leader UUID NOT NULL,
                gridX INT NOT NULL,
                gridY INT NOT NULL,
                FOREIGN KEY (leader) REFERENCES Player(playerId) ON DELETE CASCADE
            );
        """;

        String createPlayerTable = """
            CREATE TABLE Player (
                playerId UUID PRIMARY KEY,
                playerName VARCHAR(255) NOT NULL
            );
        """;

        String createMemberTable = """
            CREATE TABLE Member (
                memberId UUID PRIMARY KEY,
                islandId UUID NOT NULL,
                playerId UUID NOT NULL,
                groupType ENUM('MEMBER', 'OFFICER', 'SERGENT', 'CO_LEADER', 'LEADER') NOT NULL,
                FOREIGN KEY (islandId) REFERENCES Island(islandId) ON DELETE CASCADE,
                FOREIGN KEY (playerId) REFERENCES Player(playerId) ON DELETE CASCADE
            );
        """;

        try (PreparedStatement stmt = connection.prepareStatement(createIslandTable)) {
            stmt.execute();
        }

        try (PreparedStatement stmt = connection.prepareStatement(createPlayerTable)) {
            stmt.execute();
        }

        try (PreparedStatement stmt = connection.prepareStatement(createMemberTable)) {
            stmt.execute();
        }
    }

    public static void addMember(UUID island, UUID player) throws SQLException {
        String sql = "INSERT INTO Member (memberId, islandId, playerId, groupType) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, player.toString());
            stmt.setString(2, island.toString());
            stmt.setString(3, player.toString());
            stmt.setString(4, GroupType.MEMBER.name());
            stmt.executeUpdate();
        }
    }
    public static void removeMember(UUID island, UUID player) throws SQLException {
        String sql = "DELETE FROM Member WHERE islandId = ? AND playerId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, island.toString());
            stmt.setString(2, player.toString());
            stmt.executeUpdate();
        }
    }
    public static void changeGroup(UUID island, UUID player, GroupType group) throws SQLException {
        String sql = "UPDATE Member SET groupType = ? WHERE islandId = ? AND playerId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, group.name());
            stmt.setString(2, island.toString());
            stmt.setString(3, player.toString());
            stmt.executeUpdate();
        }
    }

    public static void createIsland(UUID island, String name, UUID leader, int x, int y) throws SQLException {
        String islandSql = "INSERT INTO Island (islandId, name, leader, gridX, gridY) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(islandSql)) {
            stmt.setString(1, island.toString());
            stmt.setString(2, name);
            stmt.setString(3, leader.toString());
            stmt.setInt(4, x);
            stmt.setInt(5, y);
            stmt.executeUpdate();
        }

        addMember(island, leader);
        changeGroup(island, leader, GroupType.LEADER);
    }
    public static void deleteIsland(UUID island) throws SQLException {
        String sql = "DELETE FROM Island WHERE islandId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, island.toString());
            stmt.executeUpdate();
        }
    }
    public static void renameIsland(UUID island, String name) throws SQLException {
        String sql = "UPDATE Island SET name = ? WHERE islandId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, island.toString());
            stmt.executeUpdate();
        }
    }
}