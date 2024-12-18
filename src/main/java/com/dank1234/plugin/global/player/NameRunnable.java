package com.dank1234.plugin.global.player;
/*
import com.dank1234.plugin.Main;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NameRunnable {
    public static void startNameRunabble() {
        //Bukkit.getScheduler().runTaskTimer(Main.get(), (task) -> Bukkit.getOnlinePlayers().forEach(NameRunnable::updatePlayer), 0L, 20*10L);
    }
    private static void updatePlayer(Player player) {
        try {
            ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
            ClientboundPlayerInfoUpdatePacket addPlayer = new ClientboundPlayerInfoUpdatePacket(
                    ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER,
                    nmsPlayer
            );

            ServerPlayer svrPlr = new ServerPlayer(
                    MinecraftServer.getServer(),
                    nmsPlayer.level().getMinecraftWorld(),
                    nmsPlayer.gameProfile,
                    nmsPlayer.clientInformation()
            );

            svrPlr.displayName = "name";

            ClientboundPlayerInfoUpdatePacket updateNamePacket = new ClientboundPlayerInfoUpdatePacket(
                    ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME,
                    svrPlr
            );

            //ServerPlayerConnection connection = nmsPlayer.connection;
            //connection.send(addPlayer);
            //connection.send(updateNamePacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

 */
