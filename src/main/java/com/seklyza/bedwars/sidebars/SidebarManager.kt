package com.seklyza.bedwars.sidebars

import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard

class SidebarManager(private val scoreboard: Scoreboard) {
    private val sidebar = scoreboard.registerNewObjective("sidebar", "dummy", "§6§lBED WARS")

    init {
        sidebar.displaySlot = DisplaySlot.SIDEBAR
    }

    fun render(lines: MutableMap<Int, String>) {
        for (entry in scoreboard.entries) {
            scoreboard.resetScores(entry)
        }

        for (line in lines) {
            sidebar.getScore(line.value).score = line.key
        }
    }
}
