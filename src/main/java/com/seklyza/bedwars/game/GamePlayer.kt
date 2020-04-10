package com.seklyza.bedwars.game

import com.seklyza.bedwars.Main
import com.seklyza.bedwars.sidebars.SidebarManager
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin.getPlugin

class GamePlayer(val player: Player, val playerState: PlayerState = PlayerState.WAITING) {
    private val plugin = getPlugin(Main::class.java)
    val sidebarManager: SidebarManager

    init {
        player.scoreboard = plugin.server.scoreboardManager.newScoreboard
        sidebarManager = SidebarManager(player.scoreboard)
    }
}
