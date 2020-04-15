package com.seklyza.bedwars.guis

import com.seklyza.bedwars.events.Event
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

abstract class GUI(name: String, size: Int) : Event(), InventoryHolder {
    @Suppress("LeakingThis")
    protected val inv: Inventory = server.createInventory(this, size, name)

    protected fun createItem(material: Material, name: String?, amount: Int, vararg lore: String): ItemStack {
        val item = ItemStack(material, amount)
        val meta = item.itemMeta
        meta.setDisplayName(name)
        meta.lore = lore.map { "§7$it" }
        item.itemMeta = meta

        return item
    }

    override fun getInventory(): Inventory {
        return inv
    }

    protected abstract fun handleOpen(e: InventoryClickEvent)

    @Suppress("unused")
    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        if (e.clickedInventory?.holder !== this) return

        e.isCancelled = true

        handleOpen(e)
    }
}
