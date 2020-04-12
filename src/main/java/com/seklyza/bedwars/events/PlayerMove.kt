package com.seklyza.bedwars.events

import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent

class PlayerMove : Event() {
    @Suppress("unused")
    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        if (e.player.location.y <= 0) {
            if (e.player.location.y <= 0) e.player.teleport(e.player.location.add(0.0, 85.0, 0.0))
            e.player.health = 0.0
        }
    }
}
