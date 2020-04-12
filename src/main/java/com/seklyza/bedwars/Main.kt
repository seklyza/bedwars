package com.seklyza.bedwars

import com.seklyza.bedwars.commands.GameCommand
import com.seklyza.bedwars.events.*
import com.seklyza.bedwars.game.Game
import net.minecraft.server.v1_15_R1.PacketPlayOutLogin
import net.minecraft.server.v1_15_R1.WorldServer
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class Main : JavaPlugin() {
    lateinit var game: Game
    lateinit var configVariables: Config

    override fun onEnable() {
        configVariables = Config()
        game = Game()
        registerCommands()
        registerEvents()
        logger.info("${description.name} has been enabled.")
    }

    override fun onDisable() {
        val world = server.getWorld("world")!!
        for (player in server.onlinePlayers) {
            player.teleport(world.spawnLocation)

            val nmsPlayer = (player as CraftPlayer).handle
            val ws = nmsPlayer.world as WorldServer
            val gameMode = nmsPlayer.playerInteractManager.gameMode
            val worldType = ws.worldData.type
            val loginPacket = PacketPlayOutLogin(nmsPlayer.id, gameMode, 0, false, ws.worldProvider.dimensionManager, 0, worldType, 0, false, false)
            nmsPlayer.playerConnection.sendPacket(loginPacket)
        }

        if (server.unloadWorld(game.gameWorld, false)) {
            logger.info("${game.gameWorld.name} world been unloaded successfully.")
        } else {
            logger.info("COULD NOT UNLOAD ${game.gameWorld.name}!!!")
        }

        logger.info("${description.name} has been disabled.")
    }

    private fun registerCommands() {
        GameCommand()
    }

    private fun registerEvents() {
        FoodLevelChange()
        BlockBreak()
        BlockPlace()
        PlayerMove()
        PlayerDeath()
        EntityDamage()
    }
}
