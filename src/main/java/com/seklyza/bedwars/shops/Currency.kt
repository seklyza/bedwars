package com.seklyza.bedwars.shops

import org.bukkit.Material

enum class Currency(val material: Material) {
    IRON(Material.IRON_INGOT),
    GOLD(Material.GOLD_INGOT),
    DIAMOND(Material.DIAMOND),
    EMERALD(Material.EMERALD);

    val displayName: String
        get() = toString().toLowerCase().capitalize()
}
