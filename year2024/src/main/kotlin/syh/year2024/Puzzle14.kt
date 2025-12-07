package syh.year2024

import kotlin.math.floor
import syh.library.AbstractAocDay
import syh.library.Coord

class Puzzle14 : AbstractAocDay(2024, 14) {
    override fun doA(file: String): String {
        val robots = readSingleLineFile(file).map { Robot.readRobot(it) }
        robots.forEach { println(it) }

        val wide = if (file == "test") 11 else 101
        val tall = if (file == "test") 7 else 103
        val steps = 100

        robots.forEach { robot ->
            var newX = (robot.position.column + (robot.velocity.column * steps)) % wide
            var newY = (robot.position.row + (robot.velocity.row * steps)) % tall
            if (newX < 0) newX += wide
            if (newY < 0) newY += tall
            val newPos = Coord(newY, newX)
            println("calculated new position for robot $robot: $newPos")
            robot.position = newPos
        }

        println()

        var quadrant1 = 0
        var quadrant2 = 0
        var quadrant3 = 0
        var quadrant4 = 0
        val middleX = floor(wide.toDouble() / 2).toInt()
        val middleY = floor(tall.toDouble() / 2).toInt()
        println("calculated middles: $middleX, $middleY")

        robots.forEach { robot ->
            if (robot.position.column == middleX || robot.position.row == middleY) {
                println("robot is in the middle: $robot")
            }
            if (robot.position.column > middleX && robot.position.row > middleY) quadrant4 += 1
            if (robot.position.column > middleX && robot.position.row < middleY) quadrant2 += 1
            if (robot.position.column < middleX && robot.position.row > middleY) quadrant3 += 1
            if (robot.position.column < middleX && robot.position.row < middleY) quadrant1 += 1
        }

        println("calculated quadrants: ")
        println("quadrant1 = $quadrant1")
        println("quadrant2 = $quadrant2")
        println("quadrant3 = $quadrant3")
        println("quadrant4 = $quadrant4")

        return (quadrant1 * quadrant2 * quadrant3 * quadrant4).toString()
    }

    override fun doB(file: String): String {
        val robots = readSingleLineFile(file).map { Robot.readRobot(it) }
        robots.forEach { println(it) }

        val wide = if (file == "test") 11 else 101
        val tall = if (file == "test") 7 else 103

        var steps = 1
        while (true) {
            robots.forEach { robot ->
                var newX = (robot.position.column + robot.velocity.column) % wide
                var newY = (robot.position.row + robot.velocity.row) % tall
                if (newX < 0) newX += wide
                if (newY < 0) newY += tall
                val newPos = Coord(newY, newX)
//                println("calculated new position for robot $robot: $newPos")
                robot.position = newPos
            }
            val positionMap = robots.groupBy { it.position }
            if (positionMap.values.all { it.size == 1 }) {
                println("found position where all robots have a unique position:")
                positionMap.forEach { println(it) }
                println("steps needed: $steps")
                return steps.toString()
            }
            steps++
        }
    }

    data class Robot(var position: Coord, val velocity: Coord) {
        companion object {
            fun readRobot(line: String): Robot {
                val split = line.split(" ")
                val (aX, aY) = split[0].split(",").map { it.removePrefix("p=") }.map { c -> c.toInt() }
                val (bX, bY) = split[1].split(",").map { it.removePrefix("v=") }.map { c -> c.toInt() }
                return Robot(Coord(aY, aX), Coord(bY, bX))
            }
        }
    }
}