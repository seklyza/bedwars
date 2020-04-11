package com.seklyza.bedwars.events

import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeath : Event() {
    @Suppress("unused")
    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        e.isCancelled = true
        e.entity.allowFlight = true
        e.entity.isFlying = true
        if (e.entity.location.y <= 0) e.entity.teleport(e.entity.location.add(0.0, 85.0, 0.0))
        for (item in e.entity.inventory.contents) {
            if (item != null) game.gameWorld.dropItemNaturally(e.entity.location, item)
        }
        e.entity.inventory.clear()
        e.entity.canPickupItems = false
    }
}
