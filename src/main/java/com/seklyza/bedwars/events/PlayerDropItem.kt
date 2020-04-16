package com.seklyza.bedwars.events

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerDropItemEvent

class PlayerDropItem : Event() {
    @Suppress("unused")
    @EventHandler
    fun onPlayerDropItem(e: PlayerDropItemEvent) {
        if (e.itemDrop.itemStack.type == Material.WOODEN_SWORD) e.isCancelled = true
    }
}
