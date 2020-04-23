package com.seklyza.bedwars.npcs

import com.seklyza.bedwars.shops.ItemsShop
import org.bukkit.Location

class ItemsShopVillagerNPC(location: Location) : VillagerNPC("§aItems Shop", location) {
    init {
        ItemsShop.entityIds.add(spawnEntity.entityID)
    }
}
