package com.seklyza.bedwars.shops

import org.bukkit.ChatColor
import org.bukkit.Material

enum class Currency(val material: Material, val color: ChatColor) {
    IRON(Material.IRON_INGOT, ChatColor.WHITE),
    GOLD(Material.GOLD_INGOT, ChatColor.GOLD),
    DIAMOND(Material.DIAMOND, ChatColor.AQUA),
    EMERALD(Material.EMERALD, ChatColor.GREEN);

    val displayName: String
        get() = toString().toLowerCase().capitalize()
}
