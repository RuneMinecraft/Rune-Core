package com.dank1234.utils.wrapper.player.staff;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;

public enum StaffRank {
    HELPER(LuckPermsProvider.get().getGroupManager().getGroup("staff.helper")),
    MOD(LuckPermsProvider.get().getGroupManager().getGroup("staff.mod")),
    SRMOD(LuckPermsProvider.get().getGroupManager().getGroup("staff.srmod")),
    ADMIN(LuckPermsProvider.get().getGroupManager().getGroup("staff.admin")),
    DEV(LuckPermsProvider.get().getGroupManager().getGroup("staff.dev")),
    MANAGER(LuckPermsProvider.get().getGroupManager().getGroup("staff.manager"));

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
