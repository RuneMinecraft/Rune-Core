package com.dank1234.plugin.skyblock;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class Member {
    private final Player player;
    private @Nullable Island island;
    private @Nullable GroupType group;

    private Member(Player player) {
        this.player = player;
    }
    public static Member of(Player player) {
        return new Member(player);
    }

    private Player player() {
        return this.player;
    }

    public @Nullable Island island() {
        return island;
    }
    public Island island(@Nullable Island island) {
        this.island = island;
        return this.island();
    }
    public @Nullable GroupType group() {
        return group;
    }
    public GroupType group(@Nullable GroupType group) {
        this.group = group;
        return this.group();
    }

    @Override
    public String toString() {
        return this.player().getName();
    }
}
