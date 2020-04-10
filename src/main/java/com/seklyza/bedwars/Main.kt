package com.seklyza.bedwars

import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class Main : JavaPlugin() {
    override fun onEnable() {
        logger.info("${description.name} has been enabled.")
    }

    override fun onDisable() {
        logger.info("${description.name} has been disabled.")
    }
}
