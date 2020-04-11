package com.seklyza.bedwars.sidebars

import com.seklyza.bedwars.game.GamePlayer

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
        if (gp.team!!.isBedAlive) {
            lm.add(prefix + "§a✔ ${if (gp.team == team) "§7YOU" else ""}")
        } else {
            if (team.players.isEmpty()) lm.add("$prefix§c✘")
            else lm.add(prefix + "§a${team.players.size} ${if (gp.team == team) "§7YOU" else ""}")
        }
    }

    return lm.add(footerSidebar())
}

fun footerSidebar(): LineManager {
    return LineManager
        .builder()
        .newLine()
        .add("§ewww.seklyza.com")
}
