package syh.year2021

import syh.library.AbstractAocDay
import syh.library.Coord

class Puzzle17 : AbstractAocDay(2021, 17) {
    override fun doA(file: String): String {
        val (xRange, yRange) = readSingleLineFile(file)[0]
            .removePrefix("target area: ")
            .split(", ")
            .map { it.split("=")[1] }
            .map { it.split("..") }
            .map { it.map { i -> i.toInt() } }
            .map { it[0]..it[1] }

        val start = Coord(0, 0)
        var maxY = 0

        for (xStartVelocity in 50 downTo 0) {
            for (yStartVelocity in 100 downTo -5) {
                var current = start
                var velocity = Coord(yStartVelocity, xStartVelocity)
                var tempMaxY = 0
                while (current.column <= xRange.last && current.row >= yRange.first) {
                    current = current.plus(velocity)
                    tempMaxY = maxOf(tempMaxY, current.row)
                    if (current.row in yRange && current.column in xRange) {
                        maxY = maxOf(tempMaxY, maxY)
                    }
                    velocity = Coord(velocity.row - 1, maxOf(velocity.column - 1, 0))
                }
            }
        }

        return maxY.toString()
    }

    override fun doB(file: String): String {
        val (xRange, yRange) = readSingleLineFile(file)[0]
            .removePrefix("target area: ")
            .split(", ")
            .map { it.split("=")[1] }
            .map { it.split("..") }
            .map { it.map { i -> i.toInt() } }
            .map { it[0]..it[1] }

        val start = Coord(0, 0)
        val winningVelocities = mutableSetOf<Coord>()

        for (xStartVelocity in 300 downTo 1) {
            for (yStartVelocity in 100 downTo -100) {
                var current = start
                var velocity = Coord(yStartVelocity, xStartVelocity)
                while (current.column <= xRange.last && current.row >= yRange.first) {
                    current = current.plus(velocity)
                    velocity = Coord(velocity.row - 1, maxOf(velocity.column - 1, 0))
                    if (current.row in yRange && current.column in xRange) {
                        winningVelocities.add(Coord(yStartVelocity, xStartVelocity))
                    }
                }
            }
        }

        return winningVelocities.size.toString()
    }

}