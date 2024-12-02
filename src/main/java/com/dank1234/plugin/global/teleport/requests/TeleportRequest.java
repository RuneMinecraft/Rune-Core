package com.dank1234.plugin.global.teleport.requests;

import org.bukkit.entity.Player;

public class TeleportRequest {
    private static final long REQUEST_TIMEOUT = 60; // SECONDS

    private final Player sender;
    private final Player recipient;

    public TeleportRequest(Player sender, Player recipient) {
        this.sender = sender;
        this.recipient = recipient;
    }

    public Player sender() {
        return this.sender;
    }
    public Player recipient() {
        return this.recipient;
    }

    public void send() {
        Thread requestThread = new Thread(() -> {

        });

        requestThread.start();
    }
}
