package com.dank1234.plugin.global.npcs.protocollib;

import com.dank1234.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.UUID;

public class NPCBuilder {
    /*
    private final String name;
    private final UUID uuid;
    private final WrappedGameProfile profile;
    private Location location;

    public NPCBuilder(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.profile = new WrappedGameProfile(uuid, name);

        //profile.getProperties().put("textures", new WrappedSignedProperty("textures", skinValue, skinSignature));
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void spawn(Player viewer) {
        if (location == null) {
            throw new IllegalStateException("Location must be set before spawning the NPC.");
        }

        PacketContainer playerInfoPacket = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        playerInfoPacket.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        playerInfoPacket.getPlayerInfoDataLists().write(0, Collections.singletonList(
                new PlayerInfoData(profile, 0, EnumWrappers.NativeGameMode.CREATIVE, WrappedChatComponent.fromText(name))
        ));

        PacketContainer spawnPacket = new PacketContainer(PacketType.Play.Server.NAMED_ENTITY_SPAWN);
        spawnPacket.getIntegers().write(0, uuid.hashCode());
        spawnPacket.getUUIDs().write(0, uuid);
        spawnPacket.getDoubles()
                .write(0, location.getX())
                .write(1, location.getY())
                .write(2, location.getZ());
        spawnPacket.getBytes()
                .write(0, (byte) (location.getYaw() * 256 / 360))
                .write(1, (byte) (location.getPitch() * 256 / 360));

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(viewer, playerInfoPacket);
            ProtocolLibrary.getProtocolManager().sendServerPacket(viewer, spawnPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bukkit.getScheduler().runTaskLater(Main.get(), () -> {
            try {
                PacketContainer playerInfoRemove = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
                playerInfoRemove.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
                playerInfoRemove.getPlayerInfoDataLists().write(0, Collections.singletonList(
                        new PlayerInfoData(profile, 0, EnumWrappers.NativeGameMode.CREATIVE, WrappedChatComponent.fromText(name))
                ));
                ProtocolLibrary.getProtocolManager().sendServerPacket(viewer, playerInfoRemove);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 20*10L);
    }

     */
}