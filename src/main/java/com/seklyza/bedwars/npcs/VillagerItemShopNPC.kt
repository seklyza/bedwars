package com.seklyza.bedwars.npcs

import com.seklyza.bedwars.shops.ItemsShop
import org.bukkit.Location
import org.bukkit.event.player.PlayerInteractAtEntityEvent

class VillagerItemShopNPC(location: Location) : VillagerNPC("§aItem Shop", location) {
    override fun handleClick(e: PlayerInteractAtEntityEvent) {
        val gp = game.players[e.player] ?: return

        gp.player.openInventory(ItemsShop(gp).inventory)
    }
}
