package com.seklyza.bedwars.game

import com.seklyza.bedwars.Main
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin.getPlugin

enum class GameTeamType(val color: ChatColor) {
    AQUA(ChatColor.AQUA),
    PINK(ChatColor.LIGHT_PURPLE),
    RED(ChatColor.RED),
    BLUE(ChatColor.BLUE);
}

class GameTeam(val type: GameTeamType) {
    companion object {
        fun getByType(type: GameTeamType): GameTeam {
            val teams = getPlugin(Main::class.java).game.teams
            return teams.find { it.type === type }!!
        }
    }

    val plugin = getPlugin(Main::class.java)
    val getSpawnPoint: (Main) -> Location = { it.configVariables.getTeamSpawnPoint(type, it.game.gameWorld) }
    val players: List<GamePlayer>
        get() {
            return plugin.game.players.filter { it.value.team == this && it.value.playerState == PlayerState.PLAYER }.values.toList()
        }

    var isBedAlive: Boolean = true
        private set

    fun destroyBed() {
        isBedAlive = false
    }
}
