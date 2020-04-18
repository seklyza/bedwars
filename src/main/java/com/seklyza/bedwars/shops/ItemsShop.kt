package com.seklyza.bedwars.shops

import com.seklyza.bedwars.game.GamePlayer
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.inventory.ItemStack

class ItemsShop(gp: GamePlayer) : Shop(gp, "Item Shop", 45) {
    companion object {
        val entityIds = mutableListOf<Int>()
    }

    init {
        addShopItem(10, gp.team!!.type.wool, 16, Currency.IRON, 4)
        addShopItem(19, Material.OAK_PLANKS, 16, Currency.IRON, 16)
        addShopItem(28, Material.END_STONE, 12, Currency.IRON, 24)

        addShopItem(12, Material.STONE_SWORD, 1, Currency.GOLD, 4)
        addShopItem(21, Material.IRON_SWORD, 1, Currency.GOLD, 7)
        addShopItem(30, Material.DIAMOND_SWORD, 1, Currency.EMERALD, 6)

        addShopItem(14, Material.BOW, 1, Currency.GOLD, 12)
        addShopItem(23, Material.ARROW, 8, Currency.GOLD, 2)
        addShopItem(32, Material.ENDER_PEARL, 1, Currency.DIAMOND, 3)

        addShopItem(16, Material.TNT, 1, Currency.GOLD, 4)
        addShopItem(25, Material.ENCHANTED_GOLDEN_APPLE, 1, Currency.EMERALD, 3)
        addShopItem(34, Material.SHIELD, 1, Currency.IRON, 15)
    }

    override fun handle(item: Item) {
        if (item.item.type == Material.IRON_SWORD) {
            gp.player.inventory.remove(Material.WOODEN_SWORD)
        }

        gp.player.inventory.addItem(ItemStack(item.item.type, item.item.amount))
        gp.player.sendMessage("§aYou bought §6${item.item.amount} §e${item.item.i18NDisplayName}§a for §6${item.price} §e${item.currency.displayName}§a!")
        gp.player.playSound(gp.player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3.toFloat(), 1.toFloat())
    }

    override fun handleError(item: Item, leftToBuy: Int) {
        gp.player.sendMessage("§cYou need §e$leftToBuy§c more§e ${item.currency.displayName}§c to buy this!")
        gp.player.playSound(gp.player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 3.toFloat(), 1.toFloat())
    }
}
