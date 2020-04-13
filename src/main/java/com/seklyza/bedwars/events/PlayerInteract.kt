package com.seklyza.bedwars.events

import com.seklyza.bedwars.game.PlayerState
import org.bukkit.block.data.type.Bed
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class PlayerInteract : Event() {
    @Suppress("unused")
    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
        val gp = game.players[e.player]

        if (gp?.state != PlayerState.PLAYER) e.isCancelled = true
        if(e.clickedBlock?.blockData is Bed && e.action == Action.RIGHT_CLICK_BLOCK) e.isCancelled = true
    }
}
