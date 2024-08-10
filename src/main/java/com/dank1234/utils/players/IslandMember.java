package com.dank1234.utils.players;

import com.dank1234.plugin.skyblock.GroupType;
import com.dank1234.plugin.skyblock.Island;
import com.dank1234.utils.data.punishments.UserManager;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class IslandMember extends User {
    private @Nullable Island island;
    private @Nullable GroupType group;

    IslandMember(UUID uuid, String username) {
        super(uuid, username);
    }
    public static IslandMember of(UUID uuid, String username) {
        return new IslandMember(uuid, username);
    }
    public static IslandMember of(UUID uuid) {
        return UserManager.getUser(uuid);
    }
    public static IslandMember of(String username) {
        return UserManager.getUser(username);
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
}
