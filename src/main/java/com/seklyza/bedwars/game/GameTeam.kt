package com.seklyza.bedwars.game

import com.seklyza.bedwars.Main
import org.bukkit.ChatColor
import org.bukkit.Location

enum class GameTeam(val color: ChatColor) {
    AQUA(ChatColor.AQUA),
    PINK(ChatColor.LIGHT_PURPLE),
    RED(ChatColor.RED),
    BLUE(ChatColor.BLUE);

    val getSpawnPoint: (Main) -> Location = { it.configVariables.getTeamSpawnPoint(this, it.game.gameWorld) }
    var isBedAlive: Boolean = true
        private set

    fun destroyBed() {
        isBedAlive = false
    }
}
