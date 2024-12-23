package com.dank1234.plugin.script.actions

import com.dank1234.plugin.Bootstrap

fun unload(script: String) {
    Bootstrap.loadedScripts.remove(script)
}