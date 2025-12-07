package syh.year2021

import syh.library.AbstractAocDay
import syh.library.Coord
import syh.library.Direction
import syh.library.Grid

class Puzzle9 : AbstractAocDay(2021, 9) {

    override fun doA(file: String): String {
        val grid = readGrid(file)

        val lowestPoints = mutableListOf<Int>()
        for (row in grid.grid.indices) {
            for (column in grid.grid[row].indices) {
                val coord = Coord(row, column)
                val coordValue = grid.at(coord)
                val neighbours = grid.findNeighbours(coord, Direction.ALL_DIRECTIONS)
                val isLowest = neighbours.none { it.second < coordValue }
                if (isLowest) {
                    lowestPoints.add(coordValue)
                }
            }
        }
        return lowestPoints.sumOf { it + 1 }.toString()
    }

    override fun doB(file: String): String {
        val grid = readGrid(file)

        val basinNodes = grid.flatten().filterNot { it.second == 9 }

        val foundNodes = mutableListOf<Coord>()
        val fullBasins = mutableListOf<MutableSet<Coord>>()

        for ((coord, _) in basinNodes) {
            if (coord in foundNodes) {
                continue
            }
            val newBasin = mutableSetOf(coord)
            val coordsToHandle = ArrayDeque<Coord>(1)
            coordsToHandle.add(coord)
            while (coordsToHandle.isNotEmpty()) {
                val coordToCheck = coordsToHandle.removeFirst()
                println("checking coord $coordToCheck")
                if (coordToCheck in foundNodes) {
                    continue
                }
                foundNodes.add(coordToCheck)
                val neighbours = grid.findNeighbours(coordToCheck, Direction.CARDINAL_DIRECTIONS)
                val basinNeighbours = neighbours.filterNot { it.second == 9 }
                coordsToHandle.addAll(basinNeighbours.map { it.first })
                newBasin.addAll(basinNeighbours.map { it.first })
            }
            fullBasins.add(newBasin)
        }
        println(fullBasins.joinToString("\n"))
        println(fullBasins.sortedByDescending { it.size }.take(3).map { it.size })
        return fullBasins.sortedByDescending { it.size }.take(3).fold(1) { prev, set -> prev * set.size }.toString()
    }

    private fun readGrid(file: String): Grid<Int> {
        val chars = readSingleLineFile(file)
            .map { it.split("").filter { c -> c.isNotEmpty() } }

        val grid = Grid<Int>()
        grid.create(chars) { it.toInt() }
        return grid
    }
}