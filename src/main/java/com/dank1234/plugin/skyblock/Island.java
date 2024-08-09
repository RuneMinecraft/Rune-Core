package com.dank1234.plugin.skyblock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Island {
    private static final World ISLAND_WORLD = Bukkit.getWorld("islands"); // THE WORLD THAT ALL THE ISLANDS ARE IN
    private static final int MAX_ISLAND_MEMBERS = 10; // THE MAX AMOUNT OF MEMBERS ON AN ISLAND
    private static final int ISLAND_SIZE = 200; // THE SIZE OF EACH ISLAND

    private final Map<Member, GroupType> members = new HashMap<>(); // EACH MEMBER AND THEIR RANK
    private final GridLocation grid = Grid.next(); // THE GRID COORDINATES (basically coords divided by 500 with no height)
    private List<Block> blockList; // EVERY BLOCK IN THE ISLANDS LOCATION

    private final UUID islandId;
    private final String name; // DEFAULTED TO "leader's Island"
    private final Member leader; // THE PERSON WHO OWNS THE ISLAND
    private final Location loc1; // FIRST CORNER
    private final Location loc2; // FAR CORNER FROM FIRST

    public Island(UUID islandId, Player player, String name) {
        this.islandId = islandId;
        this.name = name;
        this.leader = Member.of(player);
        this.loc1 = new Location(ISLAND_WORLD(), grid.getMinX(), -64, grid.getMinY());
        this.loc2 = new Location(ISLAND_WORLD(), grid.getMaxX(), 319, grid.getMaxY());
    }
    private Island(Player player, String name) {
        this(UUID.randomUUID(), player, name);
    }

    public static Island create(Player player, String name) {
        return new Island(player, name);
    }
    public static Island create(Player player) {
        return Island.create(player, "");
    }

    public void addMember(Member member) {
        if (members.size() < MAX_ISLAND_MEMBERS) {
            members.computeIfAbsent(member, Member::group);
            member.island(this);
        } else {
            // TODO: Handle full island.
            throw new IllegalStateException("Island is full. Maximum members reached.");
        }
    }
    public void addMember(GroupType group, Member member) {
        if (members.size() < MAX_ISLAND_MEMBERS) {
            members.put(member, group);
            member.island(this);
            member.group(group);
        } else {
            // TODO: Handle full island.
            throw new IllegalStateException("Island is full. Maximum members reached.");
        }
    }
    public void removeMember(Member member) {
        members.remove(member);
        member.island(null);
        member.group(null);
    }

    public Map<Member, GroupType> members() {
        return members;
    }
    public GridLocation grid() {
        return grid;
    }
    public List<Block> blockList() {
        return blockList;
    }

    public Member leader() {
        return leader;
    }
    public String name() {
        return name;
    }
    public Location loc1() {
        return loc1;
    }
    public Location loc2() {
        return loc2;
    }

    public int memberCount() {
        return members.size();
    }

    public static World ISLAND_WORLD() {
        return ISLAND_WORLD;
    }
    public static int MAX_MEMBERS() {
        return MAX_ISLAND_MEMBERS;
    }
    public static int ISLAND_SIZE() {
        return ISLAND_SIZE;
    }

    @Override
    public String toString() {
        return "Island[\n" +
                "\tname="+this.name()+"\n" +
                "\tleader="+this.leader()+"\n" +
                "\tpos1="+this.loc1()+"\n" +
                "\tpos2="+this.loc2()+"\n" +
                "]";
    }
}