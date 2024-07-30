package com.dank1234.plugin;

import com.dank1234.utils.data.Config;
import com.dank1234.utils.Logger;
import com.dank1234.utils.command.Register;
import com.dank1234.utils.data.Version;
import com.dank1234.utils.data.VersionType;
import com.dank1234.utils.server.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Main extends JavaPlugin {
    private final Version version = Version.of(VersionType.DEVELOPMENT, "0.1");
    private Server server;

    private final Register register = Register.get();
    private final Config config = Config.get();
    private static Main instance;

    public static Main get() {
        return instance;
    }

    public Version version(){
        return this.version;
    }
    public Server server() {
        return this.server;
    }
    public Register register() {
        return this.register;
    }
    public Config config() {
        return this.config;
    }

    @Override
    public void onEnable() {
        this.setNaggable(false);

        try {
            instance = this;
            server = Server.of();

            Logger.log("Plugin Enabled.");
            Logger.log("Found the config file: " + Objects.requireNonNull(this.config().find()).getCanonicalPath());

            this.register().autoRegisterCommands();
            this.register().autoRegisterListeners();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return this.register().register(sender, command, label, args);
    }
}