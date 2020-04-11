package com.seklyza.bedwars.tasks

import com.seklyza.bedwars.Main
import com.seklyza.bedwars.sidebars.ingameSidebar
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
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
    private val emeraldHolograms: List<ArmorStand>
    private val diamondHolograms: List<ArmorStand>

    init {
        diamondHolograms = diamondDropPoints.map { setupHologram(it, "§b§lDiamond") }
        emeraldHolograms = emeraldDropPoints.map { setupHologram(it, "§2§lEmerald") }
    }

    private fun setupHologram(location: Location, title: String): ArmorStand {
        val titleHologram = createHologram(location)
        titleHologram.customName = title

        return createHologram(location.subtract(0.0, 0.5, 0.0))
    }

    private fun createHologram(location: Location): ArmorStand {
        val hologram = game.gameWorld.spawnEntity(location, EntityType.ARMOR_STAND) as ArmorStand
        hologram.isVisible = false
        hologram.setGravity(false)
        hologram.isCustomNameVisible = true

        return hologram
    }

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

        // Drop emerald every a minute and a half
        dropItem(90, emeraldDropPoints, Material.EMERALD)

        secondsElapsed++
    }

    private fun dropItem(interval: Int, locations: List<Location>, material: Material) {
        val timeLeft = interval - secondsElapsed % interval
        val holograms = if (material == Material.DIAMOND) diamondHolograms else if (material == Material.EMERALD) emeraldHolograms else emptyList()

        for (hologram in holograms) {
            hologram.customName = "§eSpawns in §c$timeLeft§e seconds"
        }

        val shouldDrop = secondsElapsed >= interval && secondsElapsed % interval == 0
        if (shouldDrop) {
            for (location in locations) {
                val drop = game.gameWorld.dropItem(location, ItemStack(material))
                drop.velocity = drop.velocity.zero()
            }
        }
    }
}
