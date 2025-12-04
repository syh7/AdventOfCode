package syh.year2025

import syh.library.AbstractAocDay

class Puzzle4 : AbstractAocDay(2025, 4) {
    override fun doA(file: String): String {
        val grid = readGrid(file)
        var totalAvailableRolls = 0
        for (row in grid) {
            for (roll in row) {
                if (roll.str != "@") {
                    continue
                }
                val neighbours = findNeighbours(roll, grid)
                val rollNeighbours = neighbours.count { it.str == "@" }
                if (rollNeighbours < 4) {
                    totalAvailableRolls++
                    println("[${roll.x}][${roll.y}] is available")
                }
            }
        }
        return totalAvailableRolls.toString()
    }

    override fun doB(file: String): String {
        val grid = readGrid(file)
        var totalAvailableRolls = 0
        var changed = true
        while (changed) {
            val changedRolls = mutableListOf<Coord>()
            for (row in grid) {
                for (roll in row) {
                    if (roll.str != "@") {
                        continue
                    }
                    val neighbours = findNeighbours(roll, grid)
                    val rollNeighbours = neighbours.count { it.str == "@" }
                    if (rollNeighbours < 4) {
                        totalAvailableRolls++
                        changedRolls.add(roll)
                    }
                }
            }
            changedRolls.forEach { it.str = "." }
            changed = changedRolls.size > 0

        }
        return totalAvailableRolls.toString()
    }

    data class Coord(val x: Int, val y: Int, var str: String)

    private fun findNeighbours(start: Coord, grid: List<List<Coord>>): List<Coord> {
        val neighbours = mutableListOf<Coord>()
        for (xOffset in -1..1) {
            for (yOffset in -1..1) {
                if (yOffset == 0 && xOffset == 0) {
                    continue
                }
                val newY = start.y + yOffset
                if (newY in grid.indices && start.x + xOffset in grid[newY].indices) {
                    neighbours.add(grid[newY][start.x + xOffset])
                }
            }
        }
        return neighbours
    }

    private fun readGrid(file: String): MutableList<MutableList<Coord>> {
        val chars = readSingleLineFile(file)
            .map { it.split("").filter { c -> c.isNotEmpty() } }

        val graph = mutableListOf<MutableList<Coord>>()
        for (j in chars.indices) {
            val row = mutableListOf<Coord>()
            for (i in chars[0].indices) {
                row.add(Coord(i, j, chars[j][i]))
            }
            graph.add(row)
        }

        for (row in graph) println(row.map { it.str })
        return graph
    }
}