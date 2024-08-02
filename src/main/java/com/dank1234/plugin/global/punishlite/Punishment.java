package com.dank1234.plugin.global.punishlite;

import com.dank1234.plugin.global.punishlite.modifiers.Modifier;
import com.dank1234.utils.Locale;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.UUID;

public final class Punishment {
    private final UUID punishmentId;
    private final PunishmentType type;
    private final UUID target;
    private final UUID staff;
    private final String reason;
    private final long punishmentLength;
    private final long startTime;
    private final long endTime;
    private final Class<? extends Modifier>[] modifiers;

    public UUID punishmentId() {
        return this.punishmentId;
    }
    public PunishmentType type() {
        return this.type;
    }
    public UUID target() {
        return this.target;
    }
    public UUID staff() {
        return this.staff;
    }
    public String reason() {
        return this.reason;
    }
    public long punishmentLength() {
        return this.punishmentLength;
    }
    public long startTime() {
        return this.startTime;
    }
    public long endTime() {
        return this.endTime;
    }
    public Class<? extends Modifier>[] modifiers() {
        return this.modifiers;
    }
    public boolean active() {
        return endTime >= System.currentTimeMillis();
    }

    private Punishment(UUID punishmentId, PunishmentType type, UUID target, UUID staff, String reason, long punishmentLength, long startTime, long endTime, Class<? extends Modifier>[] modifiers) {
        this.punishmentId = punishmentId;
        this.type = type;
        this.target = target;
        this.staff = staff;
        this.reason = reason;
        this.punishmentLength = punishmentLength;
        this.startTime = startTime;
        this.endTime = endTime;
        this.modifiers = modifiers;
    }
    private Punishment(PunishmentType type, UUID target, UUID staff, String reason, long punishmentLength, long startTime, long endTime, Class<? extends Modifier>[] modifiers) {
        this.punishmentId = PunishmentUtils.generatePunishmentId();
        this.type = type;
        this.target = target;
        this.staff = staff;
        this.reason = reason;
        this.punishmentLength = punishmentLength;
        this.startTime = startTime;
        this.endTime = endTime;
        this.modifiers = modifiers;
    }
    public static @SafeVarargs Punishment of(UUID punishmentId, PunishmentType type, UUID target, UUID staff, @Nullable String reason,
                                             long punishmentLength, long endTime, Class<? extends Modifier> modifiers) {
        if (reason == null) {
            reason = Locale.DEFAULT_PUNISHMENT_REASON;
        }
        return new Punishment(punishmentId, type, target, staff, reason, punishmentLength, );
    }
    public static @SafeVarargs Punishment of(PunishmentType type, UUID target, UUID staff, @Nullable String reason, long punishmentLength, Class<? extends Modifier> ... modifiers) {
        return new Punishment(type, target, staff, reason, punishmentLength, System.currentTimeMillis(), System.currentTimeMillis()+punishmentLength, modifiers);
    }

    private String generateAlertMessage() {
        String alertMessage = "&a";
        return "";
    }

    @Override
    public String toString() {
        return "Punishment[\n"+
                "\tbanId: "+this.punishmentId()+",\n"+
                "\ttype:"+this.type()+",\n"+
                "\tplayer: "+this.target()+","+
                "\tpunisher: "+this.staff()+",\n"+
                "\treason: "+this.reason()+",\n"+
                "\tlength: "+this.punishmentLength()+",\n"+
                "\tstartTime: "+this.startTime()+",\n"+
                "\tendTime: "+this.endTime()+",\n"+
                "\tmodifiers: "+ Arrays.toString(this.modifiers()) +"\n"+
        "]";
    }
}
