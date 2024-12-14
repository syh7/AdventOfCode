package syh.year2024.day14

import syh.AbstractAocDay
import kotlin.math.floor


class Puzzle14 : AbstractAocDay(2024, 14) {
    override fun doA(file: String): String {
        val robots = readSingleLineFile(file).map { Robot.readRobot(it) }
        robots.forEach { println(it) }

        val wide = if (file == "test") 11 else 101
        val tall = if (file == "test") 7 else 103
        val steps = 100

        robots.forEach { robot ->
            var newX = (robot.position.x + (robot.velocity.x * steps)) % wide
            var newY = (robot.position.y + (robot.velocity.y * steps)) % tall
            if (newX < 0) newX += wide
            if (newY < 0) newY += tall
            val newPos = Coord(newX, newY)
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
            if (robot.position.x == middleX || robot.position.y == middleY) {
                println("robot is in the middle: $robot")
            }
            if (robot.position.x > middleX && robot.position.y > middleY) quadrant4 += 1
            if (robot.position.x > middleX && robot.position.y < middleY) quadrant2 += 1
            if (robot.position.x < middleX && robot.position.y > middleY) quadrant3 += 1
            if (robot.position.x < middleX && robot.position.y < middleY) quadrant1 += 1
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
                var newX = (robot.position.x + robot.velocity.x) % wide
                var newY = (robot.position.y + robot.velocity.y) % tall
                if (newX < 0) newX += wide
                if (newY < 0) newY += tall
                val newPos = Coord(newX, newY)
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

    data class Coord(val x: Int, val y: Int) {
        override fun toString(): String {
            return "[$x][$y]"
        }
    }

    data class Robot(var position: Coord, val velocity: Coord) {
        companion object {
            fun readRobot(line: String): Robot {
                val split = line.split(" ")
                val (aX, aY) = split[0].split(",").map { it.removePrefix("p=") }.map { c -> c.toInt() }
                val (bX, bY) = split[1].split(",").map { it.removePrefix("v=") }.map { c -> c.toInt() }
                return Robot(Coord(aX, aY), Coord(bX, bY))
            }
        }
    }
}