package com.dank1234.plugin;

import com.dank1234.plugin.global.ranks.Group
import com.dank1234.plugin.global.ranks.Track
import com.dank1234.utils.Logger;
import com.dank1234.utils.Utils;
import com.dank1234.utils.data.Config;
import com.dank1234.utils.data.Database;
import com.dank1234.utils.data.Version;
import com.dank1234.utils.data.VersionType;
import com.dank1234.utils.data.database.AuctionManager;
import com.dank1234.utils.data.database.EcoManager;
import com.dank1234.utils.data.database.StaffManager;
import com.dank1234.utils.regster.registerAllListeners
import com.dank1234.utils.regster.registerCommands
import com.dank1234.utils.server.Server;
import com.dank1234.utils.wrapper.player.User;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

class Bootstrap : Utils {
    val worlds = mutableListOf<String>()
    private val NOT_WORLDS = arrayOf("config", "crash-reports", "libraries", "versions", "logs", "plugins")
    private val version: Version = Version.of(VersionType.DEVELOPMENT, "0.1")

    private lateinit var config: Config
    lateinit var server: Server

    fun load() {
        config = Config

        Logger.infoRaw(centreText("<--------------------------------------------------------->"))
        Logger.infoRaw(centreText("<------------------> RuneMC | Rune-Core <----------------->"))
        Logger.infoRaw(centreText("<--------------------------------------------------------->"))
        Logger.infoRaw(centreText("<-----------------------> Version <----------------------->"))
        Logger.infoRaw(centreText("<------------------> $version <-------------------->"))
        Logger.infoRaw(centreText("<--------------------------------------------------------->"))

        Logger.infoRaw("[RuneMC | Bootstrap] Initializing server info...")
        server = Server.of()
    }

    fun enable() {
        registerCommands()
        registerAllListeners()

        Logger.infoRaw("[RuneMC | Bootstrap] Loading worlds...")
        loadWorlds()

        Logger.infoRaw("[RuneMC | Bootstrap] Plugin Enabled!")

        User.ensureTables()
        Group.ensureTables()
        Track.ensureTables()

        StaffManager.ensureTableExists()
        EcoManager.ensureTableExists()
        AuctionManager.ensureTableExists()

        // PermissionRunnable.start()
    }

    fun disable() {
        Logger.infoRaw("[RuneMC | Bootstrap] Disconnecting from database...")
        Runtime.getRuntime().addShutdownHook(Thread(Database::shutdown))

        Logger.infoRaw("[RuneMC | Bootstrap] Plugin Disabled!")
    }

    private fun loadWorlds() {
        val worldsFolder = Bukkit.getWorldContainer()
        if (worldsFolder.exists() && worldsFolder.isDirectory) {
            val files = worldsFolder.listFiles()
            files?.forEach { file ->
                if (NOT_WORLDS.contains(file.name)) {
                    return@forEach
                }
                if (file.isDirectory) {
                    val worldName = file.name
                    try {
                        val world = Bukkit.createWorld(WorldCreator(worldName))
                        world?.let {
                            worlds.add(worldName)
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