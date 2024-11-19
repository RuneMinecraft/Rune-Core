package com.dank1234.utils.wrapper.player.staff;

import com.dank1234.utils.data.database.StaffManager;
import com.dank1234.utils.wrapper.player.User;

import javax.annotation.Nullable;
import java.util.UUID;

@Nullable
public class Staff extends User {
    private StaffRank rank;
    private long time;
    private int messages;
    private int warns;
    private int mutes;
    private int bans;
    private boolean staffMode;

    private Staff(UUID uuid, String username, StaffRank rank, long time, int messages, int warns, int mutes, int bans, boolean staffMode) {
        super(uuid, username);
        this.rank = rank;
        this.time = time;
        this.messages = messages;
        this.warns = warns;
        this.mutes = mutes;
        this.bans = bans;
        this.staffMode = staffMode;
    }

    public static Staff of(UUID uuid, String username, StaffRank rank) {
        return new Staff(uuid, username, rank, 0, 0, 0, 0, 0, false);
    }

    public static Staff of(UUID uuid) {
        return StaffManager.getStaff(uuid).orElse(null);
    }

    public static Staff of(String username) {
        return StaffManager.getStaff(username).orElse(null);
    }

    public StaffRank rank() {
        return rank;
    }
    public Staff setRank(StaffRank rank) {
        this.rank = rank;
        return this;
    }
    public long time() {
        return time;
    }
    public Staff setTime(long time) {
        this.time = time;
        return this;
    }
    public int messages() {
        return messages;
    }
    public Staff setMessages(int messages) {
        this.messages = messages;
        return this;
    }
    public int warns() {
        return warns;
    }
    public Staff setWarns(int warns) {
        this.warns = warns;
        return this;
    }
    public int mutes() {
        return mutes;
    }
    public Staff setMutes(int mutes) {
        this.mutes = mutes;
        return this;
    }
    public int bans() {
        return bans;
    }
    public Staff setBans(int bans) {
        this.bans = bans;
        return this;
    }
    public boolean staffMode() {
        return staffMode;
    }
    public Staff setStaffMode(boolean staffMode) {
        this.staffMode = staffMode;
        return this;
    }

    @Override
    public String toString() {
        return
                "Staff[" +"\n"+
                        "    name: " + this.username() +"\n"+
                        "    uuid: " + this.uuid() +"\n"+
                        "    rank: " + this.rank +"\n"+
                        "    time: " + this.time +"\n"+
                        "    messages: " + this.messages +"\n"+
                        "    warns: " + this.warns +"\n"+
                        "    mutes: " + this.mutes +"\n"+
                        "    bans: " + this.bans +"\n"+
                        "]";
    }
}