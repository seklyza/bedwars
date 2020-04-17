package com.seklyza.bedwars.shops

import com.seklyza.bedwars.game.GamePlayer
import com.seklyza.bedwars.guis.GUI
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent

abstract class Shop(protected val gp: GamePlayer, name: String, size: Int) : GUI(name, size) {
    private val items = mutableListOf<Item>()

    protected fun addShopItem(i: Int, material: Material, amount: Int, currency: Currency, price: Int) {
        val canAfford = if (leftToBuy(price, currency) <= 0) "§aClick here to buy this item!" else "§cNot enough resources!"
        val item = Item(createItem(material, null, amount, "Cost: §e$price ${currency.color}${currency.displayName}", "", canAfford), currency, price)
        val meta = item.item.itemMeta
        meta.setDisplayName("§9${item.item.i18NDisplayName}")
        item.item.itemMeta = meta

        items.add(item)
        inv.setItem(i, item.item)
    }

    private fun leftToBuy(price: Int, currency: Currency): Int {
        var amount = 0
        gp.player.inventory.contents.filter { it?.type == currency.material }.forEach { amount += it.amount }
        return price - amount
    }

    protected abstract fun handle(item: Item)
    protected abstract fun handleError(item: Item, leftToBuy: Int)

    override fun handleOpen(e: InventoryClickEvent) {
        val item = items.find { it.item == e.currentItem } ?: return

        val leftToBuy = leftToBuy(item.price, item.currency)
        if (leftToBuy <= 0) {
            var j = item.price
            for (itemStack in gp.player.inventory) {
                if (j <= 0) break
                if (itemStack == null || itemStack.type != item.currency.material) continue

                j -= itemStack.amount
                itemStack.amount -= item.price
            }
            handle(item)
        } else handleError(item, leftToBuy)
    }
}
