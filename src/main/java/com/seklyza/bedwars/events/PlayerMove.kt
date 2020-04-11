package com.seklyza.bedwars.events

import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerMoveEvent

class PlayerMove : Event() {
    @Suppress("unused")
    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        if (e.player.location.y <= 0) {
            e.player.health = 0.0
        }
    }
}
