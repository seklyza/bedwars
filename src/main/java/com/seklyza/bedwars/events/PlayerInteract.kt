package com.seklyza.bedwars.events

import com.seklyza.bedwars.game.PlayerState
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent

class PlayerInteract : Event() {
    @Suppress("unused")
    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
        val gp = game.players[e.player]

        if (gp?.state != PlayerState.PLAYER) e.isCancelled = true
    }
}
