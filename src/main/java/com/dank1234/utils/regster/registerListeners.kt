package com.dank1234.utils.regster

import com.dank1234.plugin.Codex
import com.dank1234.plugin.Main
import com.dank1234.utils.Logger
import com.dank1234.utils.command.Event
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.reflections.Reflections
import org.reflections.scanners.TypeAnnotationsScanner

fun registerListeners() {
    val reflections = Reflections("com.dank1234.plugin", TypeAnnotationsScanner())

    val annotatedClasses = reflections.getTypesAnnotatedWith(Event::class.java, true)

    if (annotatedClasses.isEmpty()) {
        Logger.logRaw("[Bootstrap | Events] No events found.")
        return
    }

    for (clazz in annotatedClasses) {
        try {
            if (Listener::class.java.isAssignableFrom(clazz)) {
                val listener = clazz.getDeclaredConstructor().newInstance() as Listener

                Bukkit.getPluginManager().registerEvents(listener, Main.get())
                Codex.addEvent(listener)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}