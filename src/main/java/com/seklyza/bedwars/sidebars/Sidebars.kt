package com.seklyza.bedwars.sidebars

import com.seklyza.bedwars.game.GamePlayer
import com.seklyza.bedwars.game.GameTeam

fun headerSidebar(size: Int, maxPlayers: Int): LineManager {
    return LineManager
        .builder()
        .newLine()
        .add("Players: §a$size/$maxPlayers")
        .newLine()
}

fun waitingSidebar(size: Int, maxPlayers: Int): LineManager {
    return LineManager
        .builder()
        .add(headerSidebar(size, maxPlayers))
        .add("Waiting for players...")
        .add(footerSidebar())
}

fun startingSidebar(secondsLeft: Int, size: Int, maxPlayers: Int): LineManager {
    return LineManager
        .builder()
        .add(headerSidebar(size, maxPlayers))
        .add("Starting in §a$secondsLeft§r seconds")
        .add(footerSidebar())
}

fun ingameSidebar(secondsElapsed: Int, gp: GamePlayer): LineManager {
    val minutes = secondsElapsed / 60
    val seconds = secondsElapsed % 60

    val mm = if (minutes < 10) "0${minutes}" else minutes.toString()
    val ss = if (seconds < 10) "0${seconds}" else seconds.toString()

    val lm = LineManager
        .builder()
        .newLine()
        .add("Time elapsed: §a$mm:$ss")
        .newLine()

    for ((team) in gp.allTeams) {
        val prefix = "${team.color}§l${team.name.substring(0, 1)} §r${team.name.toLowerCase().capitalize()}: "
        val currentTeam = GameTeam.getByType(team)
        when {
            currentTeam.players.isEmpty() -> {
                lm.add("$prefix§c✘")
            }

            currentTeam.isBedAlive -> {
                lm.add(prefix + "§a✔ ${if (gp.team!!.type == team) "§7YOU" else ""}")
            }

            else -> {
                lm.add(prefix + "§a${currentTeam.players.size} ${if (gp.team!!.type == team) "§7YOU" else ""}")
            }
        }
    }

    return lm
        .newLine()
        .add("Kills: §a${gp.kills}")
        .add("Final kills: §a${gp.finalKills}")
        .add(footerSidebar())
}

fun footerSidebar(): LineManager {
    return LineManager
        .builder()
        .newLine()
        .add("§ewww.seklyza.com")
}
