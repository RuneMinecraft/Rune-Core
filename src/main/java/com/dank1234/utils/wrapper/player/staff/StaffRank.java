package com.dank1234.utils.wrapper.player.staff;

public enum StaffRank {
    HELPER,
    MOD,
    SRMOD,
    ADMIN,
    DEV,
    MANAGER;

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
