package com.seklyza.bedwars.events

import org.bukkit.event.EventHandler
import org.bukkit.event.entity.FoodLevelChangeEvent

class FoodLevelChange : Event() {
    @Suppress("unused")
    @EventHandler
    fun onFoodLevelChange(e: FoodLevelChangeEvent) {
        e.isCancelled = true
    }
}
