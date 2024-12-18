package com.dank1234.api.regster

import com.dank1234.plugin.Codex
import com.dank1234.plugin.Main
import com.dank1234.api.Logger
import com.dank1234.api.command.Event
import com.dank1234.api.event.EventBridge
import com.dank1234.api.event.RuneEventManager
import com.dank1234.api.event.RuneListener
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.reflections.Reflections
import org.reflections.scanners.TypeAnnotationsScanner


fun registerListeners() {
    val reflections = Reflections("com.dank1234.plugin", TypeAnnotationsScanner())

    val annotatedClasses = reflections.getTypesAnnotatedWith(Event::class.java, true)

    if (annotatedClasses.isEmpty()) {
        Logger.infoRaw("[Bootstrap | Events] No events found.")
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

fun registerAllListeners() {
    val eventManager = RuneEventManager()
    Bukkit.getPluginManager().registerEvents(EventBridge(eventManager), Main.get())

    val reflections = Reflections("com.dank1234.plugin")
    val listenerClasses = reflections.getSubTypesOf(
        RuneListener::class.java
    )

    for (listenerClass in listenerClasses) {
        try {
            val listener = listenerClass.getDeclaredConstructor().newInstance()
            eventManager.registerListener(listener)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}