package com.dank1234.plugin.global.npcs;

import com.dank1234.plugin.Main;
import com.google.gson.JsonArray;
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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class NPC {
    private static final String MOJANG_PROFILE_URL = "https://api.mojang.com/users/profiles/minecraft/";
    private static final String MOJANG_SESSION_URL = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Map<String, String> skinCache = new HashMap<>();

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

        serverPlayer.connection = new ServerGamePacketListenerImpl(
                MinecraftServer.getServer(),
                new Connection(PacketFlow.SERVERBOUND),
                serverPlayer,
                CommonListenerCookie.createInitial(profile, false)
        );

        serverPlayer.setPos(location.getX(), location.getY(), location.getZ());
        serverPlayer.setGameMode(GameType.CREATIVE);
    }

    public static CompletableFuture<NPC> create(String displayName, String skinName, Location location, Consumer<Player> clickAction) {
        NPC npc = new NPC(displayName, skinName, location, clickAction);
        return npc.initializeAndSpawn();
    }

    private CompletableFuture<NPC> initializeAndSpawn() {
        return fetchSkinTextureAsync(((GameProfile) serverPlayer.getGameProfile()), ((GameProfile) serverPlayer.getGameProfile()).getName())
                .thenRun(this::spawn)
                .thenApply(v -> this);
    }

    private void spawn() {
        serverPlayer.getCommandSenderWorld().addFreshEntity(serverPlayer, CreatureSpawnEvent.SpawnReason.COMMAND);
        Bukkit.getOnlinePlayers().forEach(this::showToPlayer);
    }

    public void showToPlayer(Player player) {
        Bukkit.getScheduler().runTask(Main.get(), () -> {
            if (!(player instanceof CraftPlayer craftPlayer)) return;

            ServerGamePacketListenerImpl connection = craftPlayer.getHandle().connection;

            ClientboundPlayerInfoUpdatePacket addPlayerPacket = clientboundPlayerInfoUpdatePacket();
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
        });

        Bukkit.getScheduler().runTaskLater(Main.get(), () -> {
            Bukkit.getOnlinePlayers().forEach(pl -> {
                if (pl instanceof CraftPlayer craftPlayer) {
                    ServerGamePacketListenerImpl connection = craftPlayer.getHandle().connection;
                    connection.send(new ClientboundPlayerInfoRemovePacket(Collections.singletonList(serverPlayer.getUUID())));
                }
            });
        }, 20L);
    }

    private @NotNull ClientboundPlayerInfoUpdatePacket clientboundPlayerInfoUpdatePacket() {
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
                Collections.singletonList(playerInfoEntry)
        );
        return addPlayerPacket;
    }

    private CompletableFuture<Void> fetchSkinTextureAsync(GameProfile profile, String skinName) {
        if (skinCache.containsKey(skinName)) {
            profile.getProperties().put("textures", new Property("textures", skinCache.get(skinName), ""));
            return CompletableFuture.completedFuture(null);
        }

        String profileUrl = MOJANG_PROFILE_URL + skinName;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(profileUrl))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenCompose(response -> {
                    if (response.statusCode() != 200) {
                        Main.get().getLogger().warning("Could not fetch profile for: " + skinName);
                        return CompletableFuture.completedFuture(null);
                    }

                    JsonObject profileJson = JsonParser.parseString(response.body()).getAsJsonObject();
                    String uuid = profileJson.get("id").getAsString();

                    String sessionUrl = MOJANG_SESSION_URL + uuid + "?unsigned=false";
                    HttpRequest sessionRequest = HttpRequest.newBuilder()
                            .uri(URI.create(sessionUrl))
                            .GET()
                            .build();

                    return httpClient.sendAsync(sessionRequest, HttpResponse.BodyHandlers.ofString());
                })
                .thenAccept(sessionResponse -> {
                    if (sessionResponse == null || sessionResponse.statusCode() != 200) {
                        Main.get().getLogger().warning("Could not fetch session for skin: " + skinName);
                        return;
                    }

                    JsonObject fullProfileJson = JsonParser.parseString(sessionResponse.body()).getAsJsonObject();
                    JsonArray properties = fullProfileJson.getAsJsonArray("properties");
                    if (properties.size() > 0) {
                        JsonObject texturesProperty = properties.get(0).getAsJsonObject();
                        String texture = texturesProperty.get("value").getAsString();
                        profile.getProperties().put("textures", new Property("textures", texture, texturesProperty.has("signature") ? texturesProperty.get("signature").getAsString() : ""));
                        skinCache.put(skinName, texture);
                        Main.get().getLogger().info("Successfully fetched skin texture for: " + skinName);
                    }
                })
                .exceptionally(e -> {
                    Main.get().getLogger().severe("Error fetching skin texture for " + skinName + ": " + e.getMessage());
                    return null;
                });
    }

    public void handleClick(Player player) {
        clickAction.accept(player);
    }
}