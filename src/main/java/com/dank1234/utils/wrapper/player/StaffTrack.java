package com.dank1234.utils.wrapper.player;

public enum StaffTrack {
    HELPER(1),
    MOD(2),
    SRMOD(3),
    ADMIN(4),
    DEV(5),
    MANAGER(6);

    private final int i;
    StaffTrack(int i) {
        this.i = i;
    }

    public int i() {
        return i;
    }
}
