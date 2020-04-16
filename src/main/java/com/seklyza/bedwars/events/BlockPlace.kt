package com.seklyza.bedwars.events

import com.seklyza.bedwars.game.GameState
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockPlaceEvent

class BlockPlace : Event() {
    private fun checkGenerator(locations: List<Location>, blockLocation: Location, distance: Double): Boolean {
        for (location in locations) {
            if (location.distance(blockLocation) <= distance) return true
        }

        return false
    }

    @Suppress("unused")
    @EventHandler
    fun onBlockPlace(e: BlockPlaceEvent) {
        if (e.block.type == Material.TNT) {
            e.block.type = Material.AIR
            e.block.world.spawnEntity(e.block.location.add(0.5, 0.0, 0.5), EntityType.PRIMED_TNT)
            return
        }

        if (game.gameState == GameState.GAME) {
            val prevent =
                checkGenerator(plugin.configVariables.getIronGoldDropPoints(game.gameWorld), e.block.location, 11.0) ||
                    checkGenerator(plugin.configVariables.getDiamondDropPoints(game.gameWorld), e.block.location, 5.0)
                    ||
                    checkGenerator(plugin.configVariables.getEmeraldDropPoints(game.gameWorld), e.block.location, 5.0)

            if (prevent) {
                e.player.sendMessage("§cError> §7You cannot place blocks near generators!")
                e.isCancelled = true

                return
            }

            game.placedBlocks.add(e.blockPlaced)
        }
    }
}
