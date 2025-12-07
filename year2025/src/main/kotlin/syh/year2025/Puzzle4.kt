package syh.year2025

import syh.library.AbstractAocDay
import syh.library.Coord
import syh.library.Grid

class Puzzle4 : AbstractAocDay(2025, 4) {
    override fun doA(file: String): String {
        val input = readSingleLineFile(file).map { it.split("").filter { c -> c.isNotEmpty() } }
        val grid = Grid<String>()
        grid.create(input) { it }
        grid.printValues()

        var totalAvailableRolls = 0
        for (row in grid.grid) {
            for ((coord, value) in row) {
                if (value != "@") {
                    continue
                }
                val neighbours = grid.findNeighboursWithValues(coord)
                val rollNeighbours = neighbours.count { it.second == "@" }
                if (rollNeighbours < 4) {
                    totalAvailableRolls++
                    println("${coord.toCoordString()} is available")
                }
            }
        }
        return totalAvailableRolls.toString()
    }

    override fun doB(file: String): String {
        val input = readSingleLineFile(file).map { it.split("").filter { c -> c.isNotEmpty() } }
        val grid = Grid<String>()
        grid.create(input) { it }
        var totalAvailableRolls = 0
        var changed = true
        var counter = 0
        while (changed) {
            println("iteration: $counter")
            val changedRolls = mutableListOf<Coord>()
            for (row in grid.grid) {
                for ((coord, value) in row) {
                    if (value != "@") {
                        continue
                    }
                    val neighbours = grid.findNeighboursWithValues(coord)
                    val rollNeighbours = neighbours.count { it.second == "@" }
                    if (rollNeighbours < 4) {
                        totalAvailableRolls++
                        changedRolls.add(coord)
                    }
                }
            }
            changedRolls.forEach { grid.set(it, ".") }
            changed = changedRolls.size > 0
            counter++
        }
        return totalAvailableRolls.toString()
    }
}