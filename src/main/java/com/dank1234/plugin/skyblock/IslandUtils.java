package com.dank1234.plugin.skyblock;

import com.dank1234.plugin.Main;
import com.dank1234.utils.data.Database;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class IslandUtils {
    private static final Database database = Main.get().database();
    private static final Connection connection = database.connection();

    public static void createTables() {
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
        }catch (Exception ignore){}

        try (PreparedStatement stmt = connection.prepareStatement(createPlayerTable)) {
            stmt.execute();
        }catch (Exception ignore){}

        try (PreparedStatement stmt = connection.prepareStatement(createMemberTable)) {
            stmt.execute();
        }catch (Exception ignore){}
    }

    public static void addMember(UUID island, UUID player) {
        String sql = "INSERT INTO Member (memberId, islandId, playerId, groupType) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, player.toString());
            stmt.setString(2, island.toString());
            stmt.setString(3, player.toString());
            stmt.setString(4, GroupType.MEMBER.name());
            stmt.executeUpdate();
        }catch (Exception ignore){}
    }
    public static void removeMember(UUID island, UUID player) {
        String sql = "DELETE FROM Member WHERE islandId = ? AND playerId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, island.toString());
            stmt.setString(2, player.toString());
            stmt.executeUpdate();
        }catch (Exception ignore){}
    }
    public static void changeGroup(UUID island, UUID player, GroupType group) {
        String sql = "UPDATE Member SET groupType = ? WHERE islandId = ? AND playerId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, group.name());
            stmt.setString(2, island.toString());
            stmt.setString(3, player.toString());
            stmt.executeUpdate();
        }catch (Exception ignore){}
    }

    public static void createIsland(UUID island, String name, UUID leader, int x, int y) {
        String islandSql = "INSERT INTO Island (islandId, name, leader, gridX, gridY) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(islandSql)) {
            stmt.setString(1, island.toString());
            stmt.setString(2, name);
            stmt.setString(3, leader.toString());
            stmt.setInt(4, x);
            stmt.setInt(5, y);
            stmt.executeUpdate();
        }catch (Exception ignore){}

        addMember(island, leader);
        changeGroup(island, leader, GroupType.LEADER);
    }
    public static void deleteIsland(UUID island) {
        String sql = "DELETE FROM Island WHERE islandId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, island.toString());
            stmt.executeUpdate();
        }catch (Exception ignore){}
    }
    public static void renameIsland(UUID island, String name) {
        String sql = "UPDATE Island SET name = ? WHERE islandId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, island.toString());
            stmt.executeUpdate();
        }catch (Exception ignore){}
    }

    public static Optional<Island> getIslandByPlayer(UUID player) {
        String sql = """
            SELECT i.islandId, i.name, i.leader, i.gridX, i.gridY
            FROM Island i
            JOIN Member m ON i.islandId = m.islandId
            WHERE m.playerId = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, player.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UUID islandId = UUID.fromString(rs.getString("islandId"));
                    String name = rs.getString("name");
                    UUID leaderId = UUID.fromString(rs.getString("leader"));
                    int gridX = rs.getInt("gridX");
                    int gridY = rs.getInt("gridY");
                    return Optional.of(new Island(islandId, name, leaderId, gridX, gridY));
                }
            }
        }catch (Exception ignore){}
        return Optional.empty();
    }
    public static Optional<Island> getIslandByLeader(UUID owner) {
        String sql = """
            SELECT islandId, name, leader, gridX, gridY
            FROM Island
            WHERE leader = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, owner.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UUID islandId = UUID.fromString(rs.getString("islandId"));
                    String name = rs.getString("name");
                    UUID leaderId = UUID.fromString(rs.getString("leader"));
                    int gridX = rs.getInt("gridX");
                    int gridY = rs.getInt("gridY");
                    return Optional.of(new Island(islandId, name, leaderId, gridX, gridY));
                }
            }
        }catch (Exception ignore){}
        return Optional.empty();
    }
    public static Optional<Island> getIslandByUUID(UUID islandId) {
        String sql = """
            SELECT islandId, name, leader, gridX, gridY
            FROM Island
            WHERE islandId = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, islandId.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("islandId"));
                    String name = rs.getString("name");
                    UUID leaderId = UUID.fromString(rs.getString("leader"));
                    int gridX = rs.getInt("gridX");
                    int gridY = rs.getInt("gridY");
                    return Optional.of(new Island(id, name, leaderId, gridX, gridY));
                }
            }
        }catch (Exception ignore){}
        return Optional.empty();
    }
}