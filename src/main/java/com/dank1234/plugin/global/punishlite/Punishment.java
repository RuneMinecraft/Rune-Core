package com.dank1234.plugin.global.punishlite;

import com.dank1234.plugin.global.punishlite.modifiers.Modifier;
import com.dank1234.plugin.global.punishlite.modifiers.Silent;
import com.dank1234.plugin.global.punishlite.players.User;
import com.dank1234.utils.DateUtils;
import com.dank1234.utils.Locale;
import com.dank1234.utils.Utils;
import com.dank1234.utils.data.punishments.PunishmentManager;
import com.dank1234.utils.data.punishments.UserManager;
import com.dank1234.utils.wrapper.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class Punishment implements Utils {
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
                                             long punishmentLength, long startTime, long endTime, Class<? extends Modifier> ... modifiers) {
        if (reason == null) {
            reason = Locale.DEFAULT_PUNISHMENT_REASON;
        }
        return new Punishment(punishmentId, type, target, staff, reason, punishmentLength, startTime, endTime, modifiers);
    }
    public static @SafeVarargs Punishment of(PunishmentType type, UUID target, UUID staff, @Nullable String reason, long punishmentLength, Class<? extends Modifier> ... modifiers) {
        if (reason == null) {
            reason = Locale.DEFAULT_PUNISHMENT_REASON;
        }
        return new Punishment(type, target, staff, reason, punishmentLength, System.currentTimeMillis(), System.currentTimeMillis()+punishmentLength, modifiers);
    }

    public void punish() {
        PunishmentManager.insert(this);
        this.sendAlert();
    }

    private void sendAlert() {
        Message.create(
                new HashSet<>(Arrays.stream(this.modifiers()).toList().contains(Silent.class)
                    ? getPlayersWithPermission("runemc.punishment.alert")
                    : Bukkit.getOnlinePlayers()).toArray(CommandSender[]::new),
                generateAlertMessage()
        ).send(false);
    }

    private String generateAlertMessage() {
        String timeMessage = "";
        String modifierMessage = "";

        if (this.punishmentLength() != -1L) {
            timeMessage = DateUtils.fromLong(1);
        }
        if (Arrays.stream(this.modifiers()).toList().contains(Silent.class)) {
            modifierMessage = "&f[&7Silent&f] &r";
        }

        return modifierMessage+"&a"+ UserManager.getUser(this.staff()).username()+" &f"+ this.type().message()+" &a"+
                UserManager.getUser(this.target()).username()+" &f"+timeMessage+"for '&a"+this.reason()+"&f'";

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
