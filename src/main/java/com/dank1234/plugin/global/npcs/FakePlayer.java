package com.dank1234.plugin.global.npcs;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class FakePlayer extends ServerPlayer {
    public FakePlayer(ServerLevel world, GameProfile profile) {
        super(MinecraftServer.getServer(), Objects.requireNonNull(MinecraftServer.getServer().getLevel(Level.OVERWORLD)), profile, ClientInformation.createDefault());
    }
}