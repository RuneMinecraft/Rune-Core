package com.dank1234.plugin;

import com.dank1234.utils.Logger;
import com.dank1234.utils.Utils;
import com.dank1234.utils.command.Register;
import com.dank1234.utils.data.Config;
import com.dank1234.utils.data.Database;
import com.dank1234.utils.data.Version;
import com.dank1234.utils.data.VersionType;
import com.dank1234.utils.data.database.AuctionManager;
import com.dank1234.utils.data.database.EcoManager;
import com.dank1234.utils.data.database.StaffManager;
import com.dank1234.utils.data.database.UserManager;
import com.dank1234.utils.server.Server;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Bootstrap implements Utils {
    List<String> worlds = new ArrayList<>();
    String[] NOT_WORLDS = new String[] {
        "config", "crash-reports", "libraries", "versions", "logs", "plugins"
    };
    Version version = Version.of(VersionType.DEVELOPMENT, "0.1");

    Register register;
    Config config;
    Server server;

    public void load() {
        config = Config.get();

        Logger.logRaw(centreText("<--------------------------------------------------------->"));
        Logger.logRaw(centreText("<------------------> RuneMC | Rune-Core <----------------->"));
        Logger.logRaw(centreText("<--------------------------------------------------------->"));
        Logger.logRaw(centreText("<-----------------------> Version <----------------------->"));
        Logger.logRaw(centreText("<------------------> "+version+" <-------------------->"));
        Logger.logRaw(centreText("<--------------------------------------------------------->"));

        Logger.logRaw("[RuneMC | Bootstrap] Loading config...");
        config.loadConfig();

        Logger.logRaw("[RuneMC | Bootstrap] Initializing server info...");
        server = Server.of();

        register = Register.get();
    }

    public void enable() {
        Logger.logRaw("[RuneMC | Bootstrap] Loading commands...");
        register.autoRegisterCommands();
        Logger.logRaw("[RuneMC | Bootstrap] Loading events...");
        register.autoRegisterListeners();
        Logger.logRaw("[RuneMC | Bootstrap] Loading worlds...");
        this.loadWorlds();

        Logger.logRaw("[RuneMC | Bootstrap] Plugin Enabled!");

        UserManager.ensureTableExists();
        StaffManager.ensureTableExists();
        EcoManager.ensureTableExists();
        AuctionManager.ensureTableExists();
    }

    public void disable() {
        Logger.logRaw("[RuneMC | Bootstrap] Disconnecting from database...");
        Runtime.getRuntime().addShutdownHook(new Thread(Database::shutdown));

        Logger.logRaw("[RuneMC | Bootstrap] Disabling all commands...");
        register.unregisterCommands();

        Logger.logRaw("[RuneMC | Bootstrap] Disabling all events...");
        register.unregisterListeners();

        Logger.logRaw("[RuneMC | Bootstrap] Plugin Disabled!");
    }

    // haha just for you laykon
    private void loadWorlds() {
        File worldsFolder = Bukkit.getWorldContainer();
        if (worldsFolder.exists() && worldsFolder.isDirectory()) {
            File[] files = worldsFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (List.of(NOT_WORLDS).contains(file.getName())) {
                        Logger.errorRaw(file.getName()+" is not a valid world!");
                        continue;
                    }
                    if (file.isDirectory()) {
                        String worldName = file.getName();
                        try {
                            Logger.logRaw("[RuneMC | Bootstrap] Loading world: " + worldName);
                            World world = Bukkit.createWorld(new WorldCreator(worldName));
                            if (world != null) {
                                worlds.add(worldName);
                                Logger.logRaw("[RuneMC | Bootstrap] Successfully loaded world: " + worldName);
                            } else {
                                Logger.errorRaw("[RuneMC | Bootstrap] Failed to load world: " + worldName);
                            }
                        } catch (Exception e) {
                            Logger.errorRaw("[RuneMC | Bootstrap] Error loading world " + worldName + ": " + e.getMessage());
                        }
                    }
                }
            }else{
                Logger.errorRaw("[RuneMC | Bootstrap] Could not retrieve worlds!");
            }
        } else {
            Logger.errorRaw("[RuneMC | Bootstrap] No worlds folder found!");
        }
    }
}