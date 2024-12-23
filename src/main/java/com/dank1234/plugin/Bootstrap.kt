package com.dank1234.plugin

import com.dank1234.plugin.global.ranks.Group
import com.dank1234.plugin.global.ranks.Ranks
import com.dank1234.plugin.global.ranks.Track
import com.dank1234.api.Logger
import com.dank1234.api.Utils
import com.dank1234.api.data.Config
import com.dank1234.api.data.Database
import com.dank1234.api.data.database.AuctionManager
import com.dank1234.api.data.database.EcoManager
import com.dank1234.api.regster.registerAllListeners
import com.dank1234.api.regster.registerCommands
import com.dank1234.api.server.Server
import com.dank1234.api.wrapper.player.User
import com.dank1234.plugin.script.ScriptManager

import org.bukkit.Bukkit
import org.bukkit.WorldCreator
import java.io.File

class Bootstrap : Utils {
    val worlds = mutableListOf<String>()
    private val FOLDERS = arrayOf("config", "crash-reports", "libraries", "versions", "logs", "plugins")

    companion object {
        @JvmStatic val loadedScripts = mutableMapOf<String, File>()
    }
    private lateinit var config: Config
    lateinit var server: Server

    fun load() {
        config = Config

        ScriptManager.initialize()

        Logger.log("""
            
           \_/=\_/=\_/=\_/=\_/=\_/=\_/=\_/=\_/=\_/=\_/==\_/=\_/=\_/=\_/=\_/=\_/=\_/=\_/=\_/=\_/=\_/
           \_/                                      RuneMC                                      \_/
           \_/                                                                                  \_/
           \_/                           insert important data here!                            \_/
           \_/                                                                                  \_/
           \_/                                                                                  \_/
           \_/                                                                                  \_/
           \_/                                                                                  \_/
           \_/                                                                                  \_/
           \_/                                                                                  \_/
           \_/=\_/=\_/=\_/=\_/=\_/=\_/=\_/=\_/=\_/=\_/==\_/=\_/=\_/=\_/=\_/=\_/=\_/=\_/=\_/=\_/=\_/
        """.trimIndent())

        Logger.infoRaw("[RuneMC | Bootstrap] Initializing server info...")
        server = Server.of()
    }

    fun enable() {
        registerCommands()
        registerAllListeners()

        for (player in Bukkit.getOnlinePlayers()) {
            Codex.addUser(User.of(player.uniqueId))
        }

        Logger.infoRaw("[RuneMC | Bootstrap] Loading worlds...")
        loadWorlds()

        Logger.infoRaw("[RuneMC | Bootstrap] Plugin Enabled!")

        User.ensureTables()
        Group.ensureTables()
        Track.ensureTables()

        // StaffManager.ensureTableExists()
        EcoManager.ensureTableExists()
        AuctionManager.ensureTableExists()

        for (rank in Ranks.entries.reversed()) {
            rank.group()
        }
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
                if (FOLDERS.contains(file.name)) {
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