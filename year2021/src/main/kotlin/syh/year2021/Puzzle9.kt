package syh.year2021

import syh.library.AbstractAocDay

class Puzzle9 : AbstractAocDay(2021, 9) {

    data class Coord(val x: Int, val y: Int, var value: Int)

    override fun doA(file: String): String {
        val grid = readGrid(file)

        val lowestPoints = mutableListOf<Coord>()
        for (y in grid.indices) {
            for (x in grid[y].indices) {
                val coord = grid[y][x]
                val neighbours = findNeighboursAllDirections(coord, grid)
                val isLowest = neighbours.none { it.value < coord.value }
                if (isLowest) {
                    lowestPoints.add(coord)
                }
            }
        }
        return lowestPoints.sumOf { it.value + 1 }.toString()
    }

    override fun doB(file: String): String {
        val grid = readGrid(file)

        val allCoords = grid.flatten()
        val basinNodes = allCoords.filterNot { it.value == 9 }
        val foundNodes = mutableListOf<Coord>()
        val fullBasins = mutableListOf<MutableSet<Coord>>()
        for (coord in basinNodes) {
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
                val neighbours = findNeighboursHorizontalVertical(coordToCheck, grid)
                val basinNeighbours = neighbours.filterNot { it.value == 9 }
                coordsToHandle.addAll(basinNeighbours)
                newBasin.addAll(basinNeighbours)
            }
            fullBasins.add(newBasin)
        }
        println(fullBasins.joinToString("\n"))
        println(fullBasins.sortedByDescending { it.size }.take(3).map { it.size })
        return fullBasins.sortedByDescending { it.size }.take(3).fold(1) { prev, set -> prev * set.size }.toString()
    }

    private fun findNeighboursAllDirections(start: Coord, grid: List<List<Coord>>): List<Coord> {
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

    private fun findNeighboursHorizontalVertical(start: Coord, grid: List<List<Coord>>): List<Coord> {
        val neighbours = mutableListOf<Coord>()
        if (start.x + 1 in grid[start.y].indices) neighbours.add(grid[start.y][start.x + 1])
        if (start.x - 1 in grid[start.y].indices) neighbours.add(grid[start.y][start.x - 1])
        if (start.y + 1 in grid.indices) neighbours.add(grid[start.y + 1][start.x])
        if (start.y - 1 in grid.indices) neighbours.add(grid[start.y - 1][start.x])
        return neighbours
    }

    private fun readGrid(file: String): MutableList<MutableList<Coord>> {
        val chars = readSingleLineFile(file)
            .map { it.split("").filter { c -> c.isNotEmpty() } }

        val graph = mutableListOf<MutableList<Coord>>()
        for (j in chars.indices) {
            val row = mutableListOf<Coord>()
            for (i in chars[0].indices) {
                row.add(Coord(i, j, chars[j][i].toInt()))
            }
            graph.add(row)
        }

        for (row in graph) println(row.map { it.value })
        return graph
    }
}