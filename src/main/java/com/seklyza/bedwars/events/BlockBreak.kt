package com.seklyza.bedwars.events

import com.seklyza.bedwars.game.GameState
import com.seklyza.bedwars.game.GameTeam
import com.seklyza.bedwars.game.GameTeamType
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent

class BlockBreak : Event() {
    private fun bedBreakHandler(e: BlockBreakEvent, teamType: GameTeamType) {
        val team = GameTeam.getByType(teamType)
        when {
            team.players.isEmpty() -> {
                e.isCancelled = true
            }

            game.players[e.player]?.team !== team -> {
                team.destroyBed()
                server.broadcastMessage("§9Game> ${team.type.color}${team.type.name.toLowerCase().capitalize()}§7's bed has been§e destroyed§7!")
                for (gp in team.players) {
                    gp.player.playSound(gp.player.location, Sound.ENTITY_WITHER_DEATH, 3.toFloat(), 1.toFloat())
                }
            }

            else -> {
                e.player.sendMessage("§cError> §7You can't break your own bed!")
                e.isCancelled = true
            }
        }
        e.isDropItems = false
    }

    @Suppress("unused")
    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        when (e.block.type) {
            Material.PINK_BED -> bedBreakHandler(e, GameTeamType.PINK)

            Material.RED_BED -> bedBreakHandler(e, GameTeamType.RED)

            Material.BLUE_BED -> bedBreakHandler(e, GameTeamType.BLUE)

            Material.LIGHT_BLUE_BED -> bedBreakHandler(e, GameTeamType.AQUA)

            else -> {
                if (game.gameState == GameState.GAME && (!game.placedBlocks.contains(e.block))) {
                    e.player.sendMessage("§cError> §7You can only break player placed blocks!")

                    e.isCancelled = true
                } else game.placedBlocks.remove(e.block)
            }
        }
    }
}
