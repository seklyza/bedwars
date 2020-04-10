package com.seklyza.bedwars.commands

import com.seklyza.bedwars.Main
import org.bukkit.command.CommandExecutor
import org.bukkit.plugin.java.JavaPlugin.getPlugin

abstract class Command(name: String) : CommandExecutor {
    protected val plugin = getPlugin(Main::class.java)
    protected val server = plugin.server
    protected val game = plugin.game

    init {
        @Suppress("LeakingThis")
        plugin.getCommand(name)!!.setExecutor(this)
    }
}
