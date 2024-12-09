package com.dank1234.plugin;

import com.dank1234.plugin.global.player.NameRunnable;
import com.dank1234.plugin.global.ranks.Group
import com.dank1234.plugin.global.ranks.Track
import com.dank1234.plugin.global.ranks.runnables.PermissionRunnable
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
import com.dank1234.utils.server.Server;
import com.dank1234.utils.server.ServerType;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class Bootstrap : Utils {
    public val worlds = mutableListOf<String>()
    private val NOT_WORLDS = arrayOf("config", "crash-reports", "libraries", "versions", "logs", "plugins")
    public val version = Version.of(VersionType.DEVELOPMENT, "0.1")

    public lateinit var register: Register
    public lateinit var config: Config
    public lateinit var server: Server

    fun load() {
        config = Config

        Logger.logRaw(centreText("<--------------------------------------------------------->"))
        Logger.logRaw(centreText("<------------------> RuneMC | Rune-Core <----------------->"))
        Logger.logRaw(centreText("<--------------------------------------------------------->"))
        Logger.logRaw(centreText("<-----------------------> Version <----------------------->"))
        Logger.logRaw(centreText("<------------------> $version <-------------------->"))
        Logger.logRaw(centreText("<--------------------------------------------------------->"))

        Logger.logRaw("[RuneMC | Bootstrap] Loading config...")
        val configData: Map<String, Any> = config.load(File("config.yml"))

        Logger.logRaw("[RuneMC | Bootstrap] Initializing server info...")
        server = Server.of()

        register = Register.get()
    }

    fun enable() {
        Logger.logRaw("[RuneMC | Bootstrap] Loading commands...")
        register.autoRegisterCommands()
        Logger.logRaw("[RuneMC | Bootstrap] Loading events...")
        register.autoRegisterListeners()
        Logger.logRaw("[RuneMC | Bootstrap] Loading worlds...")
        loadWorlds()

        Logger.logRaw("[RuneMC | Bootstrap] Plugin Enabled!")

        User.ensureTables()
        Group.ensureTables()
        Track.ensureTables()

        StaffManager.ensureTableExists()
        EcoManager.ensureTableExists()
        AuctionManager.ensureTableExists()

        if (server.TYPE() == ServerType.BOX) {
            // MineRunnable.start() // TODO: FIX THE RUNNABLE BEFORE REMOVING COMMENT
            // NameRunnable.startNameRunabble() // TODO: FIX THE RUNNABLE BEFORE REMOVING COMMENT

            PermissionRunnable.start()
        }
    }

    fun disable() {
        Logger.logRaw("[RuneMC | Bootstrap] Disconnecting from database...")
        Runtime.getRuntime().addShutdownHook(Thread(Database::shutdown))

        Logger.logRaw("[RuneMC | Bootstrap] Disabling all commands...")
        register.unregisterCommands()

        Logger.logRaw("[RuneMC | Bootstrap] Disabling all events...")
        register.unregisterListeners()

        Logger.logRaw("[RuneMC | Bootstrap] Plugin Disabled!")
    }

    private fun loadWorlds() {
        val worldsFolder = Bukkit.getWorldContainer()
        if (worldsFolder.exists() && worldsFolder.isDirectory) {
            val files = worldsFolder.listFiles()
            files?.forEach { file ->
                if (NOT_WORLDS.contains(file.name)) {
                    Logger.errorRaw("${file.name} is not a valid world!")
                    return@forEach
                }
                if (file.isDirectory) {
                    val worldName = file.name
                    try {
                        Logger.logRaw("[RuneMC | Bootstrap] Loading world: $worldName")
                        val world = Bukkit.createWorld(WorldCreator(worldName))
                        world?.let {
                            worlds.add(worldName)
                            Logger.logRaw("[RuneMC | Bootstrap] Successfully loaded world: $worldName")
                        } ?: Logger.errorRaw("[RuneMC | Bootstrap] Failed to load world: $worldName")
                    } catch (e: Exception) {
                        Logger.errorRaw("[RuneMC | Bootstrap] Error loading world $worldName: ${e.message}")
                    }
                }
            }
        } else {
        } ?: Logger.errorRaw("[RuneMC | Bootstrap] Could not retrieve worlds!")
    }
}