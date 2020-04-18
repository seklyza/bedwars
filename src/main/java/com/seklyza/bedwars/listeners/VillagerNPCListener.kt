package com.seklyza.bedwars.listeners

import com.comphenix.packetwrapper.WrapperPlayClientUseEntity
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.EnumWrappers
import com.seklyza.bedwars.Main
import com.seklyza.bedwars.shops.ItemsShop
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class VillagerNPCListener : PacketListener(ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
    override fun onPacketReceiving(e: PacketEvent) {
        val packet = WrapperPlayClientUseEntity(e.packet)
        if (packet.type != EnumWrappers.EntityUseAction.INTERACT_AT || !ItemsShop.entityIds.contains(packet.targetID)) return // Only right click

        val plugin = JavaPlugin.getPlugin(Main::class.java)

        object : BukkitRunnable() {
            override fun run() {
                val gp = plugin.game.players[e.player] ?: return
                gp.player.openInventory(ItemsShop(gp).inventory)
            }
        }.runTaskLater(plugin, 1)
    }
}
