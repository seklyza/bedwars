package com.seklyza.bedwars

import com.seklyza.bedwars.game.GameTeam
import com.seklyza.bedwars.utils.parseLocation
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.plugin.java.JavaPlugin.getPlugin

class Config {
    private val plugin = getPlugin(Main::class.java)
    private val config = plugin.config

    init {
        plugin.config.options().copyDefaults(true)
        plugin.saveConfig()
    }

    private val spawnPoints = config.getConfigurationSection("spawn_points")!!
    private val dropPointsConfigurationSection = config.getConfigurationSection("drop_points")!!
    private val getDropPoints: (String) -> (World) -> List<Location> = { type -> { world -> dropPointsConfigurationSection.getStringList(type).map { parseLocation(it)(world) } } }

    val getLobbySpawnPoint = parseLocation(config.getString("lobby_spawn_point")!!)
    val getIronGoldDropPoints = getDropPoints("iron_gold")
    val getDiamondDropPoints = getDropPoints("diamond")
    val getEmeraldDropPoints = getDropPoints("emerald")
    val lobbyPos1 = config.getString("lobby_pos1")!!.split(" ").joinToString(",")
    val lobbyPos2 = config.getString("lobby_pos2")!!.split(" ").joinToString(",")
    val getTeamSpawnPoint: (GameTeam, World) -> Location =
        { gameTeam, world ->
            parseLocation(spawnPoints.getString(gameTeam.toString().toLowerCase())!!)(world)
        }
    val minPlayers = config.getInt("min_players")
    val maxPlayers = config.getInt("max_players")
    val startCountdownTimer = config.getInt("start_countdown_timer")
}
