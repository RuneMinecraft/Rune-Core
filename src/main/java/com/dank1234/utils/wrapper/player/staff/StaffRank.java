package com.dank1234.utils.wrapper.player.staff;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.group.GroupManager;

public enum StaffRank {
    HELPER(LuckPermsProvider.get().getGroupManager().getGroup("helper")),
    MOD(LuckPermsProvider.get().getGroupManager().getGroup("mod")),
    SRMOD(LuckPermsProvider.get().getGroupManager().getGroup("srmod")),
    ADMIN(LuckPermsProvider.get().getGroupManager().getGroup("admin")),
    DEV(LuckPermsProvider.get().getGroupManager().getGroup("dev")),
    MANAGER(LuckPermsProvider.get().getGroupManager().getGroup("manager"));

    public final Group rank;

    StaffRank(Group rank) {
        this.rank = rank;
    }

    public Group getGroup() {
        return rank;
    }
    public static StaffRank getByOrdinal(int i) {
        return switch(i) {
            case 0 -> HELPER;
            case 1 -> MOD;
            case 2 -> SRMOD;
            case 3 -> ADMIN;
            case 4 -> DEV;
            case 5 -> MANAGER;
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };
    }
}
