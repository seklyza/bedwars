package com.seklyza.bedwars.events

import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent

class EntityDamage : Event() {
    @Suppress("unused")
    @EventHandler
    fun onEntityDamage(e: EntityDamageByEntityEvent) {
        if (e.damager !is Player) return

        if ((e.damager as Player).gameMode == GameMode.ADVENTURE) e.isCancelled = true
    }
}
