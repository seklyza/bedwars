package com.seklyza.bedwars.events

import com.seklyza.bedwars.game.PlayerState
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

class EntityDamage : Event() {
    @Suppress("unused")
    @EventHandler
    fun onEntityDamage(e: EntityDamageByEntityEvent) {
        if(e.damager.type == EntityType.PRIMED_TNT) e.isCancelled = true
        if (e.damager !is Player) return

        if (game.players[e.damager as Player]?.state != PlayerState.PLAYER) e.isCancelled = true
    }

    @Suppress("unused")
    @EventHandler
    fun onEntityDamage(e: EntityDamageEvent) {
        if (e.entity !is Player) return

        val gp = game.players[e.entity as Player]

        if(gp?.state != PlayerState.PLAYER) {
            e.isCancelled = true

            if(e.cause == EntityDamageEvent.DamageCause.FIRE || e.cause == EntityDamageEvent.DamageCause.FIRE_TICK) {
                e.entity.fireTicks = 0
            }
        }
    }
}
