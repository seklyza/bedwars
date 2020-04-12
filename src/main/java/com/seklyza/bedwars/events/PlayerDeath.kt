package com.seklyza.bedwars.events

import com.destroystokyo.paper.Title
import com.seklyza.bedwars.game.GameState
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class PlayerDeath : Event() {
    @Suppress("unused")
    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        if (game.gameState !== GameState.GAME) return

        e.isCancelled = true
        e.entity.gameMode = GameMode.ADVENTURE
        e.entity.allowFlight = true
        e.entity.isFlying = true
        for (item in e.entity.inventory.contents) {
            if (item != null) game.gameWorld.dropItemNaturally(e.entity.location, item)
        }
        e.entity.inventory.clear()
        e.entity.canPickupItems = false
        e.entity.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, false, false))
        e.entity.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 255, false, false))
        for (onlinePlayer in server.onlinePlayers) {
            onlinePlayer.hidePlayer(plugin, e.entity)
        }

        val gp = game.players[e.entity] ?: return
        var deathMessage = "§9Game> §7${e.deathMessage}"
        deathMessage = deathMessage.replace(gp.player.name, "${gp.team!!.type.color}${gp.player.name}§7")
        val pKiller = gp.player.killer
        if (pKiller != null) {
            val killer = game.players[pKiller]!!
            deathMessage = deathMessage.replace(killer.player.name, "${killer.team!!.type.color}${killer.player.name}§7")
            if (gp.team!!.isBedAlive) killer.kills++ else {
                killer.finalKills++
                deathMessage += " §b§lFINAL KILL!"
            }
        }

        server.broadcastMessage(deathMessage)

        if (gp.team!!.isBedAlive) {
            object : BukkitRunnable() {
                private var s = 0

                override fun run() {
                    gp.player.hideTitle()

                    if(s >= 5) {
                        cancel()

                        gp.player.teleport(gp.team!!.getSpawnPoint(plugin))
                        for (onlinePlayer in server.onlinePlayers) {
                            onlinePlayer.showPlayer(plugin, e.entity)
                        }
                        gp.player.activePotionEffects.forEach { gp.player.removePotionEffect(it.type) }
                        gp.player.gameMode = GameMode.SURVIVAL
                        gp.player.isFlying = false
                        gp.player.allowFlight = false
                        gp.player.canPickupItems = true

                        return
                    }

                    gp.player.sendTitle(Title.builder().fadeIn(0).title("§c§lYOU DIED").subtitle("§eRespawning in §c${5 - s}§e seconds").build())

                    s++
                }
            }.runTaskTimer(plugin, 0, 20)
        } else {
            gp.player.setDisplayName("§7DEAD §r${gp.player.displayName}")
            game.stopGameMaybe()
        }
    }
}
