package com.seklyza.bedwars.commands

import com.seklyza.bedwars.game.GameState
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GameCommand : Command("game") {
    override fun onCommand(sender: CommandSender, command: org.bukkit.command.Command, label: String, args: Array<out String>): Boolean {
        if (!sender.isOp) return true
        if (args.isEmpty()) return false

        when (args[0]) {
            "play" -> {
                if(sender !is Player) return false
                sender.playSound(sender.location, Sound.valueOf(args[1]), 3.toFloat(), 1.toFloat())
                return true
            }

            "start" -> {
                if(game.gameState != GameState.WAITING) return true

                return when (args.size) {
                    1 -> {
                        game.startGameCountdown()
                        true
                    }

                    2 -> {
                        try {
                            game.startGameCountdown(args[1].toInt())
                            true
                        } catch (ex: NumberFormatException) {
                            false
                        }
                    }

                    else -> false
                }
            }

            "stop" -> {
                if(game.gameState != GameState.WAITING) game.stopGameMaybe(true)
            }

            else -> return false
        }

        return true
    }
}
