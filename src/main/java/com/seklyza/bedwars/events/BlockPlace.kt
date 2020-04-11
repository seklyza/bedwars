package com.seklyza.bedwars.events

import com.seklyza.bedwars.game.GameState
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockPlaceEvent

class BlockPlace : Event() {
    @Suppress("unused")
    @EventHandler
    fun onBlockPlace(e: BlockPlaceEvent) {
        if (game.gameState == GameState.GAME)
            game.placedBlocks.add(e.blockPlaced)
    }
}
