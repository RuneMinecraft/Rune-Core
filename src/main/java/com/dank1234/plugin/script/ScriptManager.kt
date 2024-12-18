package net.runemc.plugin.script

import net.runemc.plugin.script.actions.load
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class ScriptManager(private val plugin: JavaPlugin) {
    private val scriptsFolder = File(plugin.dataFolder, "scripts")
    val loadedScripts = mutableMapOf<String, File>()

    fun initialize() {
        if (scriptsFolder.exists()) {
            return
        }
        scriptsFolder.mkdirs()

        this.getAllScripts().forEach { file ->
            load(this, file.path)
        }
    }
    private fun getAllScripts(): List<File> = scriptsFolder.walkTopDown()
        .filter { it.isFile && it.extension == "kts" }
        .toList()
    fun getAllScriptNames(): List<String> = getAllScripts()
        .map { it.relativeTo(scriptsFolder).path.replace(File.separator, "/") }
    fun getAllLoadedScriptNames(): List<String> = loadedScripts.keys.toList()
    fun getScript(name: String): File? {
        return getAllScripts().firstOrNull { it.relativeTo(scriptsFolder).path == name }
    }
}