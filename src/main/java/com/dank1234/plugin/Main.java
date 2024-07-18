package com.dank1234.plugin;

import com.dank1234.plugin.global.TestCommand;
import com.dank1234.utils.Config;
import com.dank1234.utils.Logger;
import com.dank1234.utils.command.Register;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Main extends JavaPlugin {
    private final Register register = Register.get();
    private final Config config = Config.get();
    private static Main instance;

    public static Main get() {
        return instance;
    }

    public Register register() {
        return register;
    }
    public Config config() {
        return this.config;
    }

    @Override
    public void onEnable() {
        this.setNaggable(false);
        try {
            instance = this;

            Logger.log("Plugin Enabled.");
            Logger.log("Found the config file: " + Objects.requireNonNull(this.config().find()).getCanonicalPath());

            this.register().registerCommands(new TestCommand());
            this.register().registerListeners();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, String[] args) {
        return this.register().register(sender, command, label, args);
    }
}