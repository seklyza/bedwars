package com.seklyza.bedwars.game

enum class GameState(val canStart: Boolean, val canJoin: Boolean) {
    WAITING(true, true),
    STARTING(false, true),
    GAME(false, false)
}
