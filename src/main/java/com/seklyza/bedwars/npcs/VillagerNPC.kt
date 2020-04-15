package com.seklyza.bedwars.npcs

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Villager

abstract class VillagerNPC(name: String, location: Location) : NPC(name, location, EntityType.VILLAGER) {
    protected val villager = entity as Villager

    init {
        villager.profession = Villager.Profession.NONE
        villager.villagerType = Villager.Type.PLAINS
        villager.setAdult()
        villager.setBreed(false)
        villager.ageLock = true
        villager.canPickupItems = false
        villager.isCollidable = false
        villager.isAware = false
        villager.setAI(false)
        villager.isSilent = true
    }
}
