package com.seklyza.bedwars.game

import com.seklyza.bedwars.Main
import com.seklyza.bedwars.sidebars.SidebarManager
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin.getPlugin
import org.bukkit.scoreboard.Team

class GamePlayer(val player: Player, var team: GameTeam? = null) {
    private val plugin = getPlugin(Main::class.java)
    val sidebarManager: SidebarManager
    val allTeams = mutableMapOf<GameTeamType, Team>()
    var state = PlayerState.PLAYER
    var kills = 0
    var finalKills = 0

    init {
        player.scoreboard = plugin.server.scoreboardManager.newScoreboard
        sidebarManager = SidebarManager(player.scoreboard)
    }
}
