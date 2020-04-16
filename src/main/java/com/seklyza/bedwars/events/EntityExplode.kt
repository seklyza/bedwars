package com.seklyza.bedwars.events

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityExplodeEvent

class EntityExplode : Event() {
    @Suppress("unused")
    @EventHandler
    fun onBlockExplode(e: EntityExplodeEvent) {
        e.yield = 0.toFloat()
        val it = e.blockList().iterator()
        while (it.hasNext()) {
            val block = it.next()
            if (!game.placedBlocks.contains(block)) it.remove()
        }
    }
}
