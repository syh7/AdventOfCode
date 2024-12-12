package syh.year2022.day15

import syh.AbstractAocDay
import kotlin.math.abs
import kotlin.math.max


class Puzzle15 : AbstractAocDay(2022, 15) {

    data class Coord(
        val x: Long,
        val y: Long
    ) {
        override fun toString(): String {
            return "[$x][$y]"
        }
    }

    override fun doA(file: String): String {
        val y = if (file == "test") 10L else 2_000_000L
        println("y: $y")

        val lines = readSingleLineFile(file)
        val beacons = lines.map { it.split(": ")[1] }
            .map { it.removePrefix("closest beacon is at ") }
            .map { line ->
                val (xStr, yStr) = line.split(", ")
                Coord(xStr.removePrefix("x=").toLong(), yStr.removePrefix("y=").toLong())
            }
            .toSet()
        println(beacons)

        val sensorMap = lines.map { it.split(": ") }
            .associate { (sensorStr, beaconStr) ->
                val (beaconX, beaconY) = beaconStr.removePrefix("closest beacon is at ").split(", ")
                val (sensorX, sensorY) = sensorStr.removePrefix("Sensor at ").split(", ")
                val beacon = Coord(beaconX.removePrefix("x=").toLong(), beaconY.removePrefix("y=").toLong())
                val sensor = Coord(sensorX.removePrefix("x=").toLong(), sensorY.removePrefix("y=").toLong())
                val manhattan = calculateManhattan(sensor, beacon)
                println("read sensor $sensor with manhattan $manhattan")
                sensor to manhattan
            }

        println(sensorMap)

        val allRanges = sensorMap.map { (sensor, manDis) -> calculateRange(sensor, manDis, y) }
        val mergedRanges = mergeRanges(allRanges)
        val sum = mergedRanges.sumOf { it.last - it.first }
        println("total values in summed ranges is $sum")
        return sum.toString()
    }

    override fun doB(file: String): String {
        val x = if (file == "test") 20L else 4_000_000L
        val y = if (file == "test") 20L else 4_000_000L
        println("x: $x")
        println("y: $y")

        val lines = readSingleLineFile(file)
        val beacons = lines.map { it.split(": ")[1] }
            .map { it.removePrefix("closest beacon is at ") }
            .map { line ->
                val (xStr, yStr) = line.split(", ")
                Coord(xStr.removePrefix("x=").toLong(), yStr.removePrefix("y=").toLong())
            }
            .toSet()
        println(beacons)

        val sensorMap = lines.map { it.split(": ") }
            .associate { (sensorStr, beaconStr) ->
                val (beaconX, beaconY) = beaconStr.removePrefix("closest beacon is at ").split(", ")
                val (sensorX, sensorY) = sensorStr.removePrefix("Sensor at ").split(", ")
                val beacon = Coord(beaconX.removePrefix("x=").toLong(), beaconY.removePrefix("y=").toLong())
                val sensor = Coord(sensorX.removePrefix("x=").toLong(), sensorY.removePrefix("y=").toLong())
                val manhattan = calculateManhattan(sensor, beacon)
                println("read sensor $sensor with manhattan $manhattan")
                sensor to manhattan
            }

        println(sensorMap)

        for ((sensor, manhattan) in sensorMap) {
//            println("checking sensor $sensor for distress signal")
            // check edges of the sensor
            for (xOffset in -manhattan + 1..manhattan) {
                val yOffset = manhattan - abs(xOffset)

//                println("\txOffset: $xOffset, yOffset: $yOffset")

                if (coordinateOutsideAllSensors(sensorMap, sensor.x - xOffset - 1, sensor.y + yOffset, x, y)) {
                    println("found distress signal! [${sensor.x - xOffset - 1}][${sensor.y + yOffset}]")
                    val frequency = (sensor.x - xOffset - 1) * x + sensor.y + yOffset
                    println("frequency: $frequency")
                    return frequency.toString()
                }
                if (coordinateOutsideAllSensors(sensorMap, sensor.x + xOffset + 1, sensor.y + yOffset, x, y)) {
                    println("found distress signal! [${sensor.x + xOffset + 1}][${sensor.y + yOffset}]")
                    val frequency = (sensor.x + xOffset + 1) * 4000000 + sensor.y + yOffset
                    println("frequency: $frequency")
                    return frequency.toString()
                }

            }
        }

        return "should have found something in the loop above"
    }

    private fun coordinateOutsideAllSensors(sensorMap: Map<Puzzle15.Coord, Long>, x: Long, y: Long, maxX: Long, maxY: Long): Boolean {
        if (x !in 0..maxX || y !in 0..maxY) {
            // outside of valid range
            return false
        }
        val possibleDistress = Coord(x, y)
        val inRangeOfOtherSensor = sensorMap.any { (sensor, manhattan) -> calculateManhattan(sensor, possibleDistress) <= manhattan }
        return !inRangeOfOtherSensor
    }

    private fun calculateRange(sensor: Coord, manhattanDistance: Long, y: Long): LongRange {
        val manDisY = abs(sensor.y - y)
        val leftoverManDis = manhattanDistance - manDisY
        val minus = sensor.x - leftoverManDis
        val plus = sensor.x + leftoverManDis
        val range = minus..plus
        println("calculating range for sensor $sensor with manhattan $manhattanDistance")
        println("\ty=$manDisY, thus x=$leftoverManDis, thus range is $range")
        return range
    }

    private fun mergeRanges(ranges: List<LongRange>): MutableList<LongRange> {
        // only use ranges where first is before last
        val sorted = ranges.sortedBy { it.first }.filter { it.first <= it.last }
        val newRanges = mutableListOf(sorted[0])
        for (currentRange in sorted) {
            val lastNew = newRanges.last()
            if (currentRange.first <= lastNew.last) {
                val newRange = LongRange(lastNew.first, max(currentRange.last, lastNew.last))
                println("current range $currentRange fits in $lastNew so merging to $newRange")
                newRanges.removeLast()
                newRanges.add(newRange)
            } else {
                newRanges.add(currentRange)
                println("could not merge $currentRange")
            }
        }
        return newRanges
    }

    private fun calculateManhattan(coord1: Coord, coord2: Coord): Long {
        return abs(coord1.x - coord2.x) + abs(coord1.y - coord2.y)
    }
}
