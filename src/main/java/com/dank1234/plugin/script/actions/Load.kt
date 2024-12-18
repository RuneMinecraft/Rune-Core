package net.runemc.plugin.script.actions

import net.runemc.plugin.script.ScriptManager

fun load(scriptManager: ScriptManager, script: String) {
    scriptManager.getScript(script)?.let { scriptManager.loadedScripts.putIfAbsent(script, it) }
}