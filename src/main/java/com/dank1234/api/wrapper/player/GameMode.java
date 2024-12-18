package com.dank1234.api.wrapper.player;

import org.jetbrains.annotations.NotNull;

public enum GameMode {
    SURVIVAL("Survival"),
    CREATIVE("Creative"),
    ADVENTURE("Adventure"),
    SPECTATOR("Spectator");

    private final String name;
    public @NotNull String getName() {
        return this.name;
    }

    GameMode(String name) {
        this.name = name;
    }
}
