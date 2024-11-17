package com.dank1234.utils.wrapper.player;

import com.dank1234.utils.data.punishments.StaffManager;
import com.dank1234.utils.data.punishments.UserManager;

import javax.annotation.Nullable;
import java.util.UUID;

@Nullable
public class User {
    private final UUID uuid;
    private final String username;

    User(UUID uuid, String username) {
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

    public boolean isStaff() {
        return StaffManager.getAllStaff(StaffTrack.HELPER).contains(this) || StaffManager.getAllStaff(StaffTrack.MOD).contains(this)
                || StaffManager.getAllStaff(StaffTrack.SRMOD).contains(this) || StaffManager.getAllStaff(StaffTrack.ADMIN).contains(this)
                || StaffManager.getAllStaff(StaffTrack.DEV).contains(this) || StaffManager.getAllStaff(StaffTrack.MANAGER).contains(this);
    }
}
