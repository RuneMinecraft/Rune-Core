package net.runemc.plugin.script.actions

import net.runemc.plugin.script.ScriptManager

fun unload(scriptManager: ScriptManager, script: String) {
    scriptManager.loadedScripts.remove(script)
}