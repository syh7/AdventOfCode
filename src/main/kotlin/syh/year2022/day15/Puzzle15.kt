package syh.year2022.day15

import syh.readSingleLineFile

interface Coord {
    val x: Long
    val y: Long
}

data class Beacon(override val x: Long, override val y: Long) : Coord
data class Sensor(override val x: Long, override val y: Long, val closestBeacon: Beacon) : Coord

fun main() {
    val lines = readSingleLineFile("year2022/day15/test.txt")
    val beacons = lines.map { it.split(": ")[1] }
        .map { it.removePrefix("closest beacon is at ") }
        .map { line ->
            val (xStr, yStr) = line.split(", ")
            Beacon(xStr.removePrefix("x=").toLong(), yStr.removePrefix("y=").toLong())
        }
        .toSet()
}
