package com.seklyza.bedwars.shops

import com.seklyza.bedwars.game.GamePlayer
import com.seklyza.bedwars.guis.GUI
import org.bukkit.event.inventory.InventoryClickEvent

abstract class Shop(protected val gp: GamePlayer, name: String, size: Int) : GUI(name, size) {
    private val items = mutableListOf<Item>()

    protected fun addItem(item: Item, i: Int) {
        items.add(item)
        inv.setItem(i, item.item)
    }

    private fun leftToBuy(item: Item): Int {
        var amount = 0
        gp.player.inventory.contents.filter { it?.type == item.currency.material }.forEach { amount += it.amount }
        return item.price - amount
    }

    protected abstract fun handle(item: Item)
    protected abstract fun handleError(item: Item, leftToBuy: Int)

    override fun handleOpen(e: InventoryClickEvent) {
        val item = items.find { it.item == e.currentItem } ?: return

        val leftToBuy = leftToBuy(item)
        if (leftToBuy <= 0) {
            var j = item.price
            for (itemStack in gp.player.inventory) {
                if(j <= 0) break
                if (itemStack == null || itemStack.type != item.currency.material) continue

                j -= itemStack.amount
                itemStack.amount -= item.price
            }
            handle(item)
        } else handleError(item, leftToBuy)
    }
}
