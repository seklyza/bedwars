package com.seklyza.bedwars.listeners

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.seklyza.bedwars.Main
import org.bukkit.plugin.java.JavaPlugin

abstract class PacketListener(priority: ListenerPriority, type: PacketType) : PacketAdapter(JavaPlugin.getPlugin(Main::class.java), priority, type) {
    init {
        @Suppress("LeakingThis")
        ProtocolLibrary.getProtocolManager().addPacketListener(this)
    }
}
