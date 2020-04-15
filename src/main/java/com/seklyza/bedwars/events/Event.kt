package com.seklyza.bedwars.events

import com.seklyza.bedwars.Main
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin.getPlugin

abstract class Event : Listener {
    protected val plugin = getPlugin(Main::class.java)
    protected val server = plugin.server
    protected val game = plugin.game

    init {
        @Suppress("LeakingThis")
        server.pluginManager.registerEvents(this, plugin)
    }

    fun unregister() {
        HandlerList.unregisterAll(this)
    }
}
