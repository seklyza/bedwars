package com.seklyza.bedwars.npcs

import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata
import com.comphenix.packetwrapper.WrapperPlayServerSpawnEntityLiving
import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.*

open class VillagerNPC(name: String, location: Location) {
    protected val spawnEntity = WrapperPlayServerSpawnEntityLiving()
    private val entityMetadata = WrapperPlayServerEntityMetadata()

    init {
        spawnEntity.type = 85 // Villager entity ID
        spawnEntity.entityID = (Math.random() * Int.MAX_VALUE).toInt()
        spawnEntity.x = location.x
        spawnEntity.y = location.y
        spawnEntity.z = location.z
        spawnEntity.yaw = location.yaw
        spawnEntity.pitch = location.pitch
        spawnEntity.headPitch = location.yaw

        val optionalChatComponentSerializer = WrappedDataWatcher.Registry.getChatComponentSerializer(true)
        val booleanSerializer = WrappedDataWatcher.Registry.get(Boolean::class.javaObjectType)
        val opt = Optional.of(WrappedChatComponent.fromChatMessage(name)[0].handle)
        val watcher = WrappedDataWatcher()
        watcher.setObject(WrappedDataWatcher.WrappedDataWatcherObject(2, optionalChatComponentSerializer), opt)
        watcher.setObject(WrappedDataWatcher.WrappedDataWatcherObject(3, booleanSerializer), true)

        entityMetadata.entityID = spawnEntity.entityID
        entityMetadata.metadata = watcher.watchableObjects
    }

    fun sendPacket(player: Player) {
        spawnEntity.sendPacket(player)
        entityMetadata.sendPacket(player)
    }
}
