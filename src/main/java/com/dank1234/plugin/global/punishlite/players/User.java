package com.dank1234.plugin.global.punishlite.players;

import com.dank1234.utils.data.punishments.UserManager;

import javax.annotation.Nullable;
import java.util.UUID;

@Nullable
public final class User {
    private final UUID uuid;
    private final String username;

    private User(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }
    public static User of(UUID uuid, String username) {
        return new User(uuid, username);
    }
    public static User of(UUID uuid) {
        return UserManager.getUser(uuid);
    }
    public static User of(String username) {
        return UserManager.getUser(username);
    }

    public UUID uuid() {
        return this.uuid;
    }
    public String username() {
        return this.username;
    }
}
