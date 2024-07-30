package com.dank1234.utils.wrapper.player;

import com.dank1234.utils.data.playerdata.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public final class RPlayer extends RSender {
    private final Player player;
    private final PlayerDataManager pdm;

    private RPlayer(final Player player) {
        super(player.getPlayer());
        this.player = player;
        this.pdm = PlayerDataManager.get(player);
    }

    public static RPlayer of(final Player sender) {
        return new RPlayer(sender);
    }
    public static RPlayer of(final String username) {
        return RPlayer.of(Bukkit.getPlayer(username));
    }
    public static RPlayer of(final UUID id) {
        return RPlayer.of(Bukkit.getPlayer(id));
    }

    public void force(ForceType type, String ... strings) {
        if (type.equals(ForceType.CHAT)) {
            this.chat(strings);
        } else {
            this.sudo(strings);
        }
    }
    public void sudo(String ... commands) {
        Arrays.stream(commands).toList().forEach(player::performCommand);
    }
    public void chat(String ... messages) {
        Arrays.stream(messages).toList().forEach(player::chat);
    }

    public Player player() {
        return this.player;
    }
    public PlayerDataManager playerDataManager() {
        return this.pdm;
    }
}
