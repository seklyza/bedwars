package com.seklyza.bedwars.sidebars

fun headerSidebar(size: Int, maxPlayers: Int): LineManager {
    return LineManager
        .builder()
        .newLine()
        .add("Players: §a${size}/${maxPlayers}")
        .newLine()
}

fun waitingSidebar(size: Int, maxPlayers: Int): LineManager {
    return LineManager
        .builder()
        .add(headerSidebar(size, maxPlayers))
        .add("Waiting for players...")
        .add(footerSidebar())
}

fun startingSidebar(secondsElapsed: Int, size: Int, maxPlayers: Int): LineManager {
    return LineManager
        .builder()
        .add(headerSidebar(size, maxPlayers))
        .add("Starting in §a$secondsElapsed§r seconds")
        .add(footerSidebar())
}

fun footerSidebar(): LineManager {
    return LineManager
        .builder()
        .newLine()
        .add("§ewww.seklyza.com")
}
