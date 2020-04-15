package com.seklyza.bedwars.game

import com.seklyza.bedwars.Main
import com.seklyza.bedwars.npcs.VillagerItemShopNPC
import com.seklyza.bedwars.sidebars.startingSidebar
import com.seklyza.bedwars.sidebars.waitingSidebar
import com.seklyza.bedwars.tasks.DropperTask
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin.getPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot
import kotlin.math.max
import kotlin.math.min

class Game : Listener {
    private val plugin = getPlugin(Main::class.java)
    private val config = plugin.configVariables
    private val server = plugin.server
    val gameWorld: World = server.createWorld(WorldCreator("map"))!!
    private val lobbySpawnPoint = config.getLobbySpawnPoint(gameWorld)

    lateinit var teams: MutableList<GameTeam>
    val players = mutableMapOf<Player, GamePlayer>()
    val placedBlocks = mutableListOf<Block>()
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

        player.enderChest.clear()
        for (onlinePlayer in server.onlinePlayers) {
            player.hidePlayer(plugin, onlinePlayer)
            onlinePlayer.hidePlayer(plugin, player)
            player.showPlayer(plugin, onlinePlayer)
            onlinePlayer.showPlayer(plugin, player)
        }

        val gp = GamePlayer(player)
        gp.player.setDisplayName(gp.player.name)
        players[player] = gp
        gp.player.teleport(lobbySpawnPoint)
        gp.player.gameMode = GameMode.ADVENTURE
        gp.player.health = 20.0
        gp.player.inventory.clear()
        for (activePotionEffect in gp.player.activePotionEffects) {
            gp.player.removePotionEffect(activePotionEffect.type)
        }
        gp.player.isFlying = false
        gp.player.allowFlight = false
        gp.player.canPickupItems = true
        gp.player.totalExperience = 0

        server.broadcastMessage("§8Join> §7${gp.player.name}")

        renderWaitingSidebar()

        if (players.size >= config.minPlayers && gameState.canStart) startGameCountdown()
    }

    private fun onPlayerQuit(player: Player) {
        val gp = players.remove(player)

        if (gp != null) server.broadcastMessage("§8Quit> §7${gp.player.name}")
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
        var secondsLeft = time

        val startCountdownTask = object : BukkitRunnable() {
            override fun run() {
                if (secondsLeft == 0) {
                    cancel()
                    startGame()

                    return
                }

                if (secondsLeft % 5 == 0 || secondsLeft < 5) {
                    server.broadcastMessage("§9Game> §7Starting game in §e$secondsLeft§7 seconds!")
                    for ((p) in players) {
                        p.playSound(p.location, Sound.BLOCK_NOTE_BLOCK_PLING, 3.toFloat(), 1.toFloat())
                    }
                }

                val sidebar = startingSidebar(secondsLeft, players.size, config.maxPlayers).build()
                for ((_, gp) in players) {
                    gp.sidebarManager.render(sidebar)
                }

                secondsLeft--
            }
        }
        startCountdownTask.runTaskTimer(plugin, 0, 20)
        tasks.add(startCountdownTask)
    }

    private fun startGame() {
        gameState = GameState.GAME
        server.broadcastMessage("§9Game> §7The game has been started!")
        gameWorld.time = 0

        var i = 0
        teams = GameTeamType.values().map { GameTeam(it) }.toMutableList()
        teams.shuffle()
        for ((_, gp) in players) {
            for (team in teams) {
                val sbTeam = gp.player.scoreboard.registerNewTeam(team.type.toString())
                sbTeam.color = team.type.color
                sbTeam.setAllowFriendlyFire(false)
                sbTeam.setCanSeeFriendlyInvisibles(true)
                gp.allTeams[team.type] = sbTeam
            }
            gp.team = teams[i++ % teams.size]
            gp.player.sendMessage("${gp.team!!.type.color}§lYou are in ${gp.team!!.type.name} team!!!")
            gp.player.setDisplayName("${gp.team!!.type.color}${gp.player.name}§r")
            gp.player.teleport(gp.team!!.getSpawnPoint(plugin))
            gp.player.gameMode = GameMode.SURVIVAL
            val health = gp.player.scoreboard.registerNewObjective("HP", "health", "§c♥")
            health.displaySlot = DisplaySlot.BELOW_NAME
        }

        for ((_, gp1) in players) {
            for ((_, gp2) in players) {
                gp2.allTeams[gp1.team!!.type]!!.addEntry(gp1.player.name)
                val health = gp1.player.scoreboard.getObjective("HP")!!
                health.getScore(gp1.player.name).score = gp1.player.health.toInt()
                health.getScore(gp2.player.name).score = gp2.player.health.toInt()
            }
        }

        gameWorld.pvp = true
        gameWorld.difficulty = Difficulty.NORMAL
        val pos1 = config.lobbyPos1(gameWorld)
        val pos2 = config.lobbyPos2(gameWorld)
        val minX = min(pos1.x.toInt(), pos2.x.toInt())
        val minY = min(pos1.y.toInt(), pos2.y.toInt())
        val minZ = min(pos1.z.toInt(), pos2.z.toInt())
        val maxX = max(pos1.x.toInt(), pos2.x.toInt())
        val maxY = max(pos1.y.toInt(), pos2.y.toInt())
        val maxZ = max(pos1.z.toInt(), pos2.z.toInt())

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val block = gameWorld.getBlockAt(x, y, z)

                    if (block.type != Material.AIR) block.type = Material.AIR
                }
            }
        }

        val dropperTask = DropperTask()
        dropperTask.runTaskTimer(plugin, 0, 20)
        tasks.add(dropperTask)

        spawnNPCs()
    }

    private fun spawnNPCs() {
        config.getItemShops(gameWorld).map { VillagerItemShopNPC(it) }
    }

    fun stopGameMaybe(force: Boolean = false) {
        if (gameState == GameState.GAME) {
            val teamsLeft = teams.filter { it.players.isNotEmpty() }
            if (teamsLeft.size <= 1 || force) {
                announceEnding()
                if (teamsLeft.size == 1) server.broadcastMessage("§9Game> ${teamsLeft[0].type.color}${teamsLeft[0].type.name.toLowerCase().capitalize()}§7 team has won the game!")
                gameWorld.pvp = false

                server.onlinePlayers.forEach {
                    it.gameMode = GameMode.ADVENTURE
                    it.inventory.clear()
                    players[it]?.state = PlayerState.SPECTATOR
                }

                for (task in tasks) {
                    if (!task.isCancelled) task.cancel()
                }
                tasks = mutableListOf()

                object : BukkitRunnable() {
                    override fun run() {
                        server.pluginManager.disablePlugin(plugin)
                        server.pluginManager.enablePlugin(plugin)
                    }
                }.runTaskLater(plugin, 20 * 5)
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
