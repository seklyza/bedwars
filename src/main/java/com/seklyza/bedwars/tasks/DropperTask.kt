package com.seklyza.bedwars.tasks

import com.seklyza.bedwars.Main
import com.seklyza.bedwars.sidebars.ingameSidebar
import org.bukkit.plugin.java.JavaPlugin.getPlugin
import org.bukkit.scheduler.BukkitRunnable

class DropperTask : BukkitRunnable() {
    private var secondsElapsed = 0
    private val plugin = getPlugin(Main::class.java)
    private val game = plugin.game

    override fun run() {
        for ((_, gp) in game.players) {
            gp.sidebarManager.render(ingameSidebar(secondsElapsed, gp).build())
        }

        secondsElapsed++
    }
}
