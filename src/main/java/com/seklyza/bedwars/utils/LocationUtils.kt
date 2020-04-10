package com.seklyza.bedwars.utils

import org.bukkit.Location
import org.bukkit.World

fun parseLocation(location: String, delimiter: String = " "): (world: World) -> Location {
    val axes = location.split(delimiter)

    var x = 0.toDouble()
    var y = 0.toDouble()
    var z = 0.toDouble()
    var yaw = 0.toFloat()
    var pitch = 0.toFloat()
    for ((i, axis) in axes.withIndex()) {
        when (i) {
            0 -> x = axis.toDouble()
            1 -> y = axis.toDouble()
            2 -> z = axis.toDouble()
            3 -> yaw = axis.toFloat()
            4 -> pitch = axis.toFloat()
        }
    }

    return {
        Location(it, x, y, z, yaw, pitch)
    }
}
