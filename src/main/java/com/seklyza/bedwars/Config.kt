package com.seklyza.bedwars

import com.seklyza.bedwars.utils.parseLocation
import org.bukkit.plugin.java.JavaPlugin.getPlugin

class Config {
    private val plugin = getPlugin(Main::class.java)
    private val config = plugin.config

    init {
        plugin.config.options().copyDefaults(true)
        plugin.saveConfig()
    }

    val getLobbySpawnPoint = parseLocation(config.getString("lobby_spawn_point")!!)
    val minPlayers = config.getInt("min_players")
    val maxPlayers = config.getInt("max_players")
    val startCountdownTimer = config.getInt("start_countdown_timer")
}
