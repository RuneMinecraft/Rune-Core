package com.dank1234.utils.wrapper.player;

import com.dank1234.utils.data.playerdata.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.UUID;

@Nullable
public final class RunePlayer extends RuneSender {
    private final Player player;
    private final PlayerDataManager pdm;

    private RunePlayer(final Player player) {
        super(player.getPlayer());
        this.player = player;
        this.pdm = PlayerDataManager.get(player);
    }

    public static RunePlayer of(final Player sender) {
        return new RunePlayer(sender);
    }
    public static RunePlayer of(final String username) {
        return RunePlayer.of(Bukkit.getPlayer(username));
    }
    public static RunePlayer of(final UUID id) {
        return RunePlayer.of(Bukkit.getPlayer(id));
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

    public void gamemode(GameMode gamemode) {
        this.player().setGameMode(gamemode);
    }

    public Player player() {
        return this.player;
    }
    public PlayerDataManager playerDataManager() {
        return this.pdm;
    }
}
