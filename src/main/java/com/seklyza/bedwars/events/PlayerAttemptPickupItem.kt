package com.seklyza.bedwars.events

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerAttemptPickupItemEvent

class PlayerAttemptPickupItem : Event() {
    @Suppress("unused")
    @EventHandler
    fun onPlayerAttemptPickupItem(e: PlayerAttemptPickupItemEvent) {
        val it = e.item.itemStack

        if(it.type == Material.IRON_SWORD ||
            it.type == Material.GOLDEN_SWORD ||
            it.type == Material.DIAMOND_SWORD) {
            e.player.inventory.remove(Material.WOODEN_SWORD)
        }
    }
}
