package com.dank1234.plugin;

import com.dank1234.utils.data.Config;
import com.dank1234.utils.command.Register;
import com.dank1234.utils.data.Version;
import com.dank1234.utils.server.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class Main extends JavaPlugin {
    private final Bootstrap bootstrap = new Bootstrap();

    private static Main instance;
    public static Main get() {
        return instance;
    }

    public Version version(){
        return this.bootstrap.version;
    }
    public Server server() {
        return this.bootstrap.server;
    }
    public Register register() {
        return this.bootstrap.register;
    }
    public Config config() {
        return this.bootstrap.config;
    }

    @Override
    public void onLoad() {
        try {
            instance = this;

            this.setNaggable(false);
            bootstrap.load();
        }catch(InvalidPluginException ignore) {}
    }

    @Override
    public void onEnable() {
        bootstrap.enable();
    }

    @Override
    public void onDisable() {
        bootstrap.disable();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return this.register().register(sender, command, label, args);
    }
}