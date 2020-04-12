package com.seklyza.bedwars.events

import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PlayerDeath : Event() {
    @Suppress("unused")
    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        e.isCancelled = true
        e.entity.allowFlight = true
        e.entity.isFlying = true
        for (item in e.entity.inventory.contents) {
            if (item != null) game.gameWorld.dropItemNaturally(e.entity.location, item)
        }
        e.entity.inventory.clear()
        e.entity.canPickupItems = false
        e.entity.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, false, false))
        for (onlinePlayer in server.onlinePlayers) {
            onlinePlayer.hidePlayer(plugin, e.entity)
        }
    }
}
