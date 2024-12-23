package com.dank1234.plugin.script.actions

import com.dank1234.plugin.Bootstrap
import com.dank1234.plugin.script.ScriptManager

fun load(script: String) {
    ScriptManager.getScript(script)?.let { Bootstrap.loadedScripts.putIfAbsent(script, it) }
}