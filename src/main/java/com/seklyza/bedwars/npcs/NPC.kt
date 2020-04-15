package com.seklyza.bedwars.npcs

import com.seklyza.bedwars.events.Event
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.bukkit.inventory.EquipmentSlot

abstract class NPC(name: String, location: Location, type: EntityType) : Event() {
    protected val entity = location.world.spawnEntity(location, type)

    init {
        entity.isInvulnerable = true
        entity.setGravity(false)
        entity.customName = name
        entity.isCustomNameVisible = true
    }

    protected abstract fun handleClick(e: PlayerInteractAtEntityEvent)

    @Suppress("unused")
    @EventHandler
    fun onPlayerInteract(e: PlayerInteractAtEntityEvent) {
        if (e.rightClicked.entityId != entity.entityId || e.hand != EquipmentSlot.HAND) return

        e.isCancelled = true

        handleClick(e)
    }
}
