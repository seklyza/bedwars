package com.seklyza.bedwars.tasks

import com.seklyza.bedwars.Main
import com.seklyza.bedwars.sidebars.ingameSidebar
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin.getPlugin
import org.bukkit.scheduler.BukkitRunnable

class DropperTask : BukkitRunnable() {
    private var secondsElapsed = 0
    private val plugin = getPlugin(Main::class.java)
    private val config = plugin.configVariables
    private val game = plugin.game
    private val ironGoldDropPoints = config.getIronGoldDropPoints(game.gameWorld)
    private val diamondDropPoints = config.getDiamondDropPoints(game.gameWorld)
    private val emeraldDropPoints = config.getEmeraldDropPoints(game.gameWorld)

    override fun run() {
        for ((_, gp) in game.players) {
            gp.sidebarManager.render(ingameSidebar(secondsElapsed, gp).build())
        }

        // Drop iron every two seconds
        dropItem(2, ironGoldDropPoints, Material.IRON_INGOT)

        // Drop gold every six seconds
        dropItem(6, ironGoldDropPoints, Material.GOLD_INGOT)

        // Drop diamond every thirty seconds
        dropItem(30, diamondDropPoints, Material.DIAMOND)

        // Drop emerald every one and a half seconds
        dropItem(90, emeraldDropPoints, Material.EMERALD)

        secondsElapsed++
    }

    private fun dropItem(interval: Int, locations: List<Location>, material: Material) {
        if(secondsElapsed >= interval && secondsElapsed % interval == 0) {
            for (location in locations) {
                val drop = game.gameWorld.dropItem(location, ItemStack(material))
                drop.velocity = drop.velocity.zero()
            }
        }
    }
}
