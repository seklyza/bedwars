package com.seklyza.bedwars.shops

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class Item(val item: ItemStack, val currency: Currency, val price: Int)
