package com.dank1234.plugin.skyblock;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Island {
    private static final int MAX_MEMBERS = 10;

    private final Map<Member, GroupType> members = new HashMap<>();
    private Member leader;

    private final String name;
    private final Location location;

    private Island(Player player, String name, Location location) {
        this.name = name;
        this.location = location;

        this.leader = Member.of(player);
    }
    public static Island create(Player player, String name, Location loc) {
        return new Island(player, name, loc);
    }
    public static Island create(Player player, Location loc) {
        return Island.create(player, "", loc);
    }

    public void addMember(Member member) {
        if (members.size() < MAX_MEMBERS) {
            members.computeIfAbsent(member, Member::group);
            member.island(this);
        } else {
            // TODO: Handle full island.
            throw new IllegalStateException("Island is full. Maximum members reached.");
        }
    }
    public void addMember(GroupType group, Member member) {
        if (members.size() < MAX_MEMBERS) {
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

    public String name() {
        return name;
    }
    public Location location() {
        return location;
    }
    public int memberCount() {
        return members.size();
    }
    public static int maxMembers() {
        return MAX_MEMBERS;
    }
}