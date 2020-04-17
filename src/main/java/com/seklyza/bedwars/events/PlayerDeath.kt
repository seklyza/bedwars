package com.seklyza.bedwars.events

import com.destroystokyo.paper.Title
import com.seklyza.bedwars.game.GameState
import com.seklyza.bedwars.game.PlayerState
import com.seklyza.bedwars.shops.Currency
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class PlayerDeath : Event() {
    @Suppress("unused")
    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        e.isCancelled = true
        if (game.gameState !== GameState.GAME) return
        val gp = game.players[e.entity] ?: return

        e.entity.gameMode = GameMode.ADVENTURE
        e.entity.allowFlight = true
        e.entity.isFlying = true
        val resources = countResources(e.entity)
        e.entity.inventory.clear()
        e.entity.canPickupItems = false
        e.entity.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, 99999, 255, false, false))
        e.entity.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 255, false, false))
        for (onlinePlayer in server.onlinePlayers) {
            onlinePlayer.hidePlayer(plugin, e.entity)
        }

        var deathMessage = "§9Game> §7${e.deathMessage}"
        deathMessage = deathMessage.replace(gp.player.name, "${gp.team!!.type.color}${gp.player.name}§7")
        val pKiller = gp.player.killer
        if (pKiller != null) {
            pKiller.playSound(pKiller.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3.toFloat(), 1.toFloat())

            val killer = game.players[pKiller]!!
            deathMessage = deathMessage.replace(killer.player.name, "${killer.team!!.type.color}${killer.player.name}§7")
            server.broadcastMessage(deathMessage)
            for ((currency, amount) in resources) {
                pKiller.sendMessage("${currency.color}+$amount ${currency.displayName}")
                pKiller.inventory.addItem(ItemStack(currency.material, amount))
            }
            if (gp.team!!.isBedAlive) killer.kills++ else {
                killer.finalKills++
                deathMessage += " §b§lFINAL KILL!"
            }
        } else server.broadcastMessage(deathMessage)

        if (gp.team!!.isBedAlive) {
            gp.state = PlayerState.RESPAWNING
            object : BukkitRunnable() {
                private var s = 0

                override fun run() {
                    gp.player.hideTitle()

                    if (s >= 5) {
                        cancel()

                        for (onlinePlayer in server.onlinePlayers) {
                            onlinePlayer.showPlayer(plugin, e.entity)
                        }
                        gp.player.activePotionEffects.forEach { gp.player.removePotionEffect(it.type) }
                        gp.player.isFlying = false
                        gp.player.allowFlight = false
                        gp.player.canPickupItems = true
                        gp.player.gameMode = GameMode.SURVIVAL
                        gp.player.teleport(gp.team!!.getSpawnPoint(plugin))
                        gp.player.sendTitle(Title.builder().fadeIn(0).title("§a§lYOU RESPAWNED!").fadeOut(5).build())

                        // We set the player to be a player again only 0.5 seconds afterwards because then the player could take fall damage (see EntityDamage#onEntityDamage(EntityDamageEvent))
                        object : BukkitRunnable() {
                            override fun run() {
                                gp.state = PlayerState.PLAYER
                            }
                        }.runTaskLater(plugin, 10)

                        return
                    }

                    gp.player.sendTitle(Title.builder().fadeIn(0).title("§c§lYOU DIED").subtitle("§eRespawning in §c${5 - s}§e seconds").build())

                    s++
                }
            }.runTaskTimer(plugin, 0, 20)
        } else {
            gp.player.setDisplayName("§7DEAD §r${gp.player.displayName}")
            gp.state = PlayerState.SPECTATOR
            game.stopGameMaybe()
        }
    }

    private fun countResources(player: Player): Map<Currency, Int> {
        val currencies = mutableMapOf<Material, Currency>()
        val resources = mutableMapOf<Currency, Int>()
        for (currency in Currency.values()) {
            currencies[currency.material] = currency
        }
        for (itemStack in player.inventory.contents) {
            if (itemStack == null) continue

            if (currencies[itemStack.type] != null) {
                if (resources[currencies[itemStack.type]!!] == null) {
                    resources[currencies[itemStack.type]!!] = 0
                }

                resources[currencies[itemStack.type]!!] = resources[currencies[itemStack.type]!!]!!.plus(itemStack.amount)
            }
        }

        return resources
    }
}
