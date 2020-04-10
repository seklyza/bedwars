package com.seklyza.bedwars.game

import com.seklyza.bedwars.Main
import com.seklyza.bedwars.sidebars.startingSidebar
import com.seklyza.bedwars.sidebars.waitingSidebar
import org.bukkit.Difficulty
import org.bukkit.GameMode
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin.getPlugin
import org.bukkit.scheduler.BukkitRunnable

class Game : Listener {
    private val plugin = getPlugin(Main::class.java)
    private val config = plugin.configVariables
    private val server = plugin.server
    val gameWorld: World = server.createWorld(WorldCreator("map"))!!
    private val lobbySpawnPoint = config.getLobbySpawnPoint(gameWorld)

    private val players = mutableMapOf<Player, GamePlayer>()
    private var tasks = mutableListOf<BukkitRunnable>()
    var gameState = GameState.WAITING

    init {
        gameWorld.isAutoSave = false
        gameWorld.difficulty = Difficulty.PEACEFUL
        gameWorld.pvp = false
        object : BukkitRunnable() {
            override fun run() {
                for (player in server.onlinePlayers) {
                    onPlayerJoin(player)
                }
            }
        }.runTaskLater(plugin, 10)

        server.pluginManager.registerEvents(this, plugin)
    }

    @Suppress("unused")
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        e.joinMessage = null
        onPlayerJoin(e.player)
    }

    @Suppress("unused")
    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        e.quitMessage = null
        onPlayerQuit(e.player)
    }

    private fun onPlayerJoin(player: Player) {
        if (players.size + 1 >= config.maxPlayers || !gameState.canJoin) {
            player.kickPlayer("Game has already started")
            return
        }

        for (onlinePlayer in server.onlinePlayers) {
            player.showPlayer(plugin, onlinePlayer)
            onlinePlayer.showPlayer(plugin, player)
        }

        val gp = GamePlayer(player)
        players[player] = gp
        gp.player.teleport(lobbySpawnPoint)
        gp.player.gameMode = GameMode.ADVENTURE
        gp.player.inventory.clear()
        for (activePotionEffect in gp.player.activePotionEffects) {
            gp.player.removePotionEffect(activePotionEffect.type)
        }

        server.broadcastMessage("§8Join> §7${gp.player.name}")

        renderWaitingSidebar()

        if (players.size >= config.minPlayers && gameState.canStart) startGameCountdown()
    }

    private fun onPlayerQuit(player: Player) {
        val gp = players.remove(player)!!

        server.broadcastMessage("§8Quit> §7${gp.player.name}")
        stopGameMaybe()
    }


    private fun renderWaitingSidebar() {
        if (gameState == GameState.WAITING) {
            val sidebar = waitingSidebar(players.size, config.maxPlayers).build()
            for ((_, gp) in players) {
                gp.sidebarManager.render(sidebar)
            }
        }
    }

    fun startGameCountdown(time: Int = config.startCountdownTimer) {
        gameState = GameState.STARTING
        var secondsElapsed = time

        val startCountdownTask = object : BukkitRunnable() {
            override fun run() {
                if (secondsElapsed == 0) {
                    cancel()
                    startGame()

                    return
                }

                if (secondsElapsed % 5 == 0 || secondsElapsed < 5)
                    server.broadcastMessage("§9Game> §7Starting game in $secondsElapsed seconds!")

                val sidebar = startingSidebar(secondsElapsed, players.size, config.maxPlayers).build()
                for ((_, gp) in players) {
                    gp.sidebarManager.render(sidebar)
                }

                secondsElapsed--
            }
        }
        startCountdownTask.runTaskTimer(plugin, 0, 20)
        tasks.add(startCountdownTask)
    }

    private fun startGame() {
        gameState = GameState.GAME
        server.broadcastMessage("§9Game> §7The game has been started!")
    }

    fun stopGameMaybe(force: Boolean = false) {
        if (gameState == GameState.GAME) {
            if (players.size == 1 || force) {
                announceEnding()
                server.reload()
            }
        } else if (gameState == GameState.STARTING && (players.size < config.minPlayers || force)) {
            for (task in tasks) {
                if (!task.isCancelled) task.cancel()
            }
            tasks = mutableListOf()

            gameState = GameState.WAITING
            renderWaitingSidebar()
            announceEnding()
        }
    }

    private fun announceEnding() {
        server.broadcastMessage("§9Game> §7The game has ended!")
    }
}
