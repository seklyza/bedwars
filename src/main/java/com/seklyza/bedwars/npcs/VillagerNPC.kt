package com.seklyza.bedwars.npcs

import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType

open class VillagerNPC(name: String, location: Location) : WrapperPlayServerSpawnEntityLiving() {
    init {
        type = 85 // Villager entity ID
        entityID = (Math.random() * Int.MAX_VALUE).toInt()
        x = location.x
        y = location.y
        z = location.z
        yaw = location.yaw
        pitch = location.pitch
        headPitch = location.yaw

        val armorStand = location.world.spawnEntity(location, EntityType.ARMOR_STAND) as ArmorStand
        armorStand.isVisible = false
        armorStand.setGravity(false)
        armorStand.isCustomNameVisible = true
        armorStand.customName = name
    }
}
