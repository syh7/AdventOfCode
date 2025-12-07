package syh.year2021

import syh.library.AbstractAocDay
import syh.library.Coord
import syh.library.Direction

class Puzzle11 : AbstractAocDay(2021, 11) {
    override fun doA(file: String): String {
        val map = readSingleLineFile(file).map { line -> line.map { it.digitToInt() } }

        var grid = map
        var totalFlashes = 0

        for (i in 1..100) {

            val (newGrid, newFlashes) = flashoctopuses(grid)
            totalFlashes += newFlashes

            setOctopusesToZero(newGrid)

            grid = newGrid
            println("step $i, totalFlashes = $totalFlashes")
            grid.map { println(it.joinToString("")) }

        }

        return totalFlashes.toString()
    }

    override fun doB(file: String): String {
        val map = readSingleLineFile(file).map { line -> line.map { it.digitToInt() } }

        var grid = map
        var totalFlashes = 0
        var steps = 0

        val totalOctopuses = grid.size * grid[0].size
        println("Total octopuses = $totalOctopuses")

        while (totalFlashes != totalOctopuses) {
            steps += 1

            val (newGrid, newFlashes) = flashoctopuses(grid)
            totalFlashes = newFlashes

            setOctopusesToZero(newGrid)

            grid = newGrid
            println("step $steps, totalFlashes = $totalFlashes")
            grid.map { println(it.joinToString("")) }

        }

        return steps.toString()
    }

    private fun flashoctopuses(grid: List<List<Int>>): Pair<MutableList<MutableList<Int>>, Int> {
        var totalFlashes = 0
        val newGrid = mutableListOf<MutableList<Int>>()
        var triggeredOctopuses = mutableListOf<Coord>()

        for (row in grid.indices) {
            newGrid.add(mutableListOf())
            for (column in grid[row].indices) {
                val newValue = grid[row][column] + 1
                newGrid[row].add(newValue)
                if (newValue == 10) {
                    triggeredOctopuses.add(Coord(row, column))
                    totalFlashes += 1
                }
            }
        }

        while (triggeredOctopuses.isNotEmpty()) {
            val newTriggered = mutableListOf<Coord>()

            for (coord in triggeredOctopuses) {
                Direction.ALL_DIRECTIONS.map { coord.relative(it) }
                    .filter { (row, column) -> row in grid.indices && column in grid[row].indices }
                    .map { (row, column) ->
                        val newValue = newGrid[row][column] + 1
                        newGrid[row][column] = newValue
                        if (newValue == 10) {
                            newTriggered.add(Coord(row, column))
                            totalFlashes += 1
                        }
                    }
            }

            triggeredOctopuses = newTriggered
        }
        return Pair(newGrid, totalFlashes)
    }

    private fun setOctopusesToZero(newGrid: MutableList<MutableList<Int>>) {
        for (row in newGrid.indices) {
            for (column in newGrid[row].indices) {
                if (newGrid[row][column] > 9) {
                    newGrid[row][column] = 0
                }
            }
        }
    }

}