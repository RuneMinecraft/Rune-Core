package com.dank1234.plugin.global.punishlite;

public enum PunishmentType {
    KICK("kicked"),
    WARN("muted"),
    UNWARN("unwarned"),
    MUTE("muted"),
    UNMUTE("unmuted"),
    BAN("banned"),
    UNBAN("unbanned");

    private final String message;

    PunishmentType(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
