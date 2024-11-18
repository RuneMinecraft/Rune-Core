package com.dank1234.utils.wrapper.player;

import com.dank1234.utils.Logger;
import com.dank1234.utils.data.database.UserManager;

import javax.annotation.Nullable;
import java.util.UUID;

@Nullable
public class User {
    private final UUID uuid;
    private final String username;

    protected User(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }
    public static User of(UUID uuid, String username) {
        return new User(uuid, username);
    }
    public static User of(UUID uuid) {
        return UserManager.getUser(uuid).orElse(null);
    }
    public static User of(String username) {
        return UserManager.getUser(username).orElse(null);
    }

    public UUID uuid() {
        return this.uuid;
    }
    public String username() {
        return this.username;
    }

    @Override
    public String toString() {
        return
                "User[" +
                    "\tname: "+this.username()+
                    "\tuuid: "+this.uuid()+
                "]";
    }
}
