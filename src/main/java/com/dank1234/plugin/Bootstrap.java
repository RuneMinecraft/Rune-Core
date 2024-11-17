package com.dank1234.plugin;

import com.dank1234.utils.Logger;
import com.dank1234.utils.Utils;
import com.dank1234.utils.command.Register;
import com.dank1234.utils.data.Config;
import com.dank1234.utils.data.Database;
import com.dank1234.utils.data.Version;
import com.dank1234.utils.data.VersionType;
import com.dank1234.utils.server.Server;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginLoader;

@SuppressWarnings({"removal"})
public class Bootstrap implements Utils {
    private static PluginLoader pluginLoader;
    public static PluginLoader pluginLoader() {
        return Bootstrap.pluginLoader;
    }

    final Version version = Version.of(VersionType.DEVELOPMENT, "0.1");
    final Database database = Database.of("runemc");
    final Register register = Register.get();
    final Config config = Config.get();
    Server server;

    public void load() throws InvalidPluginException {
        pluginLoader = Main.get().getPluginLoader();

        Logger.logRaw(centreText("<--------------------------------------------------------->"));
        Logger.logRaw(centreText("<------------------> RuneMC | Rune-Core <----------------->"));
        Logger.logRaw(centreText("<--------------------------------------------------------->"));
        Logger.logRaw(centreText("<-----------------------> Authors <----------------------->"));
        Logger.logRaw(centreText("<---> dank1234 - DuckLord - nLaykon - Encrypted_funds <--->"));
        Logger.logRaw(centreText("<--------------------------------------------------------->"));
        Logger.logRaw(centreText("<-----------------------> Version <----------------------->"));
        Logger.logRaw(centreText("<------------------> "+version+" <-------------------->"));
        Logger.logRaw(centreText("<--------------------------------------------------------->"));

        Logger.logRaw("[RuneMC | Bootstrap] Loading config...");
        config.loadConfig();
        Logger.logRaw("[RuneMC | Bootstrap] Initializing server info...");
        server = Server.of();
    }

    public void enable() {
        Logger.logRaw("[RuneMC | Bootstrap] Loading commands...");
        register.autoRegisterCommands();
        Logger.logRaw("[RuneMC | Bootstrap] Loading events...");
        register.autoRegisterListeners();

        Logger.logRaw("[RuneMC | Bootstrap] Plugin Enabled!");
    }

    public void disable() {
        Logger.logRaw("[RuneMC | Bootstrap] Disabling all commands...");
        register.unregisterCommands();
        Logger.logRaw("[RuneMC | Bootstrap] Disabling all events...");
        register.unregisterListeners();
        Logger.logRaw("[RuneMC | Bootstrap] Plugin Disabled!");
    }
}

