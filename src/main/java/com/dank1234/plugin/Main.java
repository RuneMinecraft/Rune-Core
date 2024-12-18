package com.dank1234.plugin;

import com.dank1234.api.regster.RegisterCommandsKt;
import com.dank1234.api.server.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Main extends JavaPlugin {
    private final Bootstrap bootstrap = new Bootstrap();

    private static Main instance;
    public static Main get() {
        return instance;
    }

    public List<String> worlds() {
        return this.bootstrap.getWorlds();
    }
    public Server server() {
        return this.bootstrap.server;
    }

    @Override
    public void onLoad() {
        try {
            instance = this;

            this.setNaggable(false);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        try {
            bootstrap.load();
            bootstrap.enable();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        try {
            bootstrap.disable();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return RegisterCommandsKt.execute(sender, command, args);
    }
}