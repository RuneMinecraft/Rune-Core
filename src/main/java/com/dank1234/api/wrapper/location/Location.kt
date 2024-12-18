package com.dank1234.api.wrapper.location

import org.bukkit.World
import java.util.*
import kotlin.jvm.Throws

open class Location(
    val world: World,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
) {
    companion object {
        @JvmStatic @Throws(Exception::class) fun of(world: World, x: Double, y: Double, z: Double, yaw: Float, pitch: Float): Optional<Location> {
            return Optional.of(Location(world, x, y, z, yaw, pitch))
        }
        @JvmStatic @Throws(Exception::class) fun of(loc: org.bukkit.Location): Optional<Location> {
            return Optional.of(Location(loc.world, loc.x, loc.y, loc.z, loc.yaw, loc.pitch))
        }
    }
}