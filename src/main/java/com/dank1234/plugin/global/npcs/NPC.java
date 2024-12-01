package com.dank1234.plugin.global.npcs;

import com.dank1234.plugin.Main;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.checkerframework.checker.units.qual.C;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class NPC {
    private final ServerPlayer serverPlayer;
    private final Consumer<Player> clickAction;

    private NPC(String displayName, String skinName, Location location, Consumer<Player> clickAction) {
        this.clickAction = clickAction;

        ServerLevel nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile profile = new GameProfile(UUID.randomUUID(), displayName);

        serverPlayer = new ServerPlayer(
                ((CraftServer) Bukkit.getServer()).getServer(),
                nmsWorld,
                profile,
                ClientInformation.createDefault()
        );

        addSkinToProfile(profile, skinName);

        serverPlayer.connection = new ServerGamePacketListenerImpl(
                MinecraftServer.getServer(),
                new Connection(PacketFlow.SERVERBOUND),
                serverPlayer,
                CommonListenerCookie.createInitial(profile, false)
        );

        serverPlayer.setPos(location.getX(), location.getY(), location.getZ());
        serverPlayer.setGameMode(GameType.CREATIVE);
    }

    public static NPC create(String displayName, String skinName, Location location, Consumer<Player> clickAction) {
        NPC npc = new NPC(displayName, skinName, location, clickAction);
        npc.spawn();
        return npc;
    }

    private void spawn() {
        serverPlayer.getCommandSenderWorld().addFreshEntity(serverPlayer, CreatureSpawnEvent.SpawnReason.COMMAND);
        Bukkit.getOnlinePlayers().forEach(this::showToPlayer);
    }

    public void showToPlayer(Player player) {
        Bukkit.getScheduler().runTask(Main.get(), () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer instanceof CraftPlayer) {
                    ServerGamePacketListenerImpl connection = ((CraftPlayer) onlinePlayer).getHandle().connection;

                    ClientboundPlayerInfoUpdatePacket.Entry playerInfoEntry = new ClientboundPlayerInfoUpdatePacket.Entry(
                            serverPlayer.getUUID(),
                            serverPlayer.getGameProfile(),
                            false,
                            0,
                            GameType.CREATIVE,
                            Component.literal(serverPlayer.getName().getString()),
                            0,
                            null
                    );

                    ClientboundPlayerInfoUpdatePacket addPlayerPacket = new ClientboundPlayerInfoUpdatePacket(
                            EnumSet.of(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER),
                            List.of(playerInfoEntry)
                    );
                    connection.send(addPlayerPacket);

                    ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(
                            serverPlayer.getId(),
                            serverPlayer.getUUID(),
                            serverPlayer.getX(),
                            serverPlayer.getY(),
                            serverPlayer.getZ(),
                            serverPlayer.getXRot(),
                            serverPlayer.getYRot(),
                            serverPlayer.getType(),
                            0,
                            Vec3.ZERO,
                            serverPlayer.yHeadRot
                    );
                    connection.send(addEntityPacket);
                }
            }
        });

        Bukkit.getScheduler().runTaskLater(Main.get(), () -> {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer instanceof CraftPlayer) {
                    ServerGamePacketListenerImpl connection = ((CraftPlayer) onlinePlayer).getHandle().connection;
                    connection.send(new ClientboundPlayerInfoRemovePacket(Collections.singletonList(serverPlayer.getUUID())));
                }
            }
        }, 20L);
    }

    private void addSkinToProfile(GameProfile profile, String skinName) {
        String texture = fetchSkinTexture(skinName);
        if (texture != null) {
            System.out.println(texture);
            profile.getProperties().put("textures", new Property("textures", texture, ""));
            serverPlayer.gameProfile = profile;
        }else{
            profile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTczMzAxNDc1NDYxOCwKICAicHJvZmlsZUlkIiA6ICJmMGU3YzIzYjkzYTc0OTQ1YjgyYzZiMjFiYjk1MjdiNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJkYW5rMTIzNCIsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8zMWY0NzdlYjFhN2JlZWU2MzFjMmNhNjRkMDZmOGY2OGZhOTNhMzM4NmQwNDQ1MmFiMjdmNDNhY2RmMWI2MGNiIgogICAgfQogIH0KfQ==", ""));
        }
    }

    private String fetchSkinTexture(String skinName) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + skinName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            if (connection.getResponseCode() != 200) {
                Bukkit.getLogger().warning("Could not fetch skin for: " + skinName);
                return null;
            }
            JsonObject profileJson = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
            String uuid = profileJson.get("id").getAsString();

            URL profileUrl = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            connection = (HttpURLConnection) profileUrl.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() != 200) {
                Bukkit.getLogger().warning("Could not fetch profile for UUID: " + uuid);
                return null;
            }
            JsonObject fullProfileJson = JsonParser.parseReader(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
            JsonObject properties = fullProfileJson.getAsJsonArray("properties").get(0).getAsJsonObject();

            return properties.get("value").getAsString();
        } catch (Exception e) {
            Bukkit.getLogger().severe("Error fetching skin texture for " + skinName + ": " + e.getMessage());
            return null;
        }
    }

    public void handleClick(Player player) {
        clickAction.accept(player);
    }
}
