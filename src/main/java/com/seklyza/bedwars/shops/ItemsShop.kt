package com.seklyza.bedwars.shops

import com.seklyza.bedwars.game.GamePlayer
import org.bukkit.Material
import org.bukkit.Sound

class ItemsShop(gp: GamePlayer) : Shop(gp, "Item Shop", 54) {
    init {
        addItem(Item(createItem(gp.team!!.type.wool, null, 16), Currency.IRON, 4), 20)
        addItem(Item(createItem(Material.IRON_SWORD, null, 1), Currency.GOLD, 7), 22)
        addItem(Item(createItem(Material.END_STONE, null, 12), Currency.IRON, 24), 29)
    }

    override fun handle(item: Item) {
        if(item.item.type == Material.IRON_SWORD) {
            gp.player.inventory.remove(Material.WOODEN_SWORD)
        }

        gp.player.inventory.addItem(item.item)
        gp.player.sendMessage("§aYou bought §6${item.item.amount} §e${item.item.i18NDisplayName}§a for §6${item.price} §e${item.currency.displayName}§a!")
        gp.player.playSound(gp.player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3.toFloat(), 1.toFloat())
    }

    override fun handleError(item: Item, leftToBuy: Int) {
        gp.player.sendMessage("§cYou need §e$leftToBuy§c more§e ${item.currency.displayName}§c to buy this!")
        gp.player.playSound(gp.player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 3.toFloat(), 1.toFloat())
    }
}
