package com.seklyza.bedwars.events

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemStack

class PlayerDropItem : Event() {
    @Suppress("unused")
    @EventHandler
    fun onPlayerDropItem(e: PlayerDropItemEvent) {
        if (e.itemDrop.itemStack.type == Material.WOODEN_SWORD) e.isCancelled = true
        else if (e.player.inventory.contents.filterNotNull().none {
                it.type == Material.WOODEN_SWORD ||
                    it.type == Material.IRON_SWORD ||
                    it.type == Material.GOLDEN_SWORD ||
                    it.type == Material.DIAMOND_SWORD
            }) {
            e.player.inventory.addItem(ItemStack(Material.WOODEN_SWORD))
        }
    }
}
