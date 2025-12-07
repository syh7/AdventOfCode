package syh.year2021

import syh.library.AbstractAocDay
import syh.library.Coord
import syh.library.Direction
import syh.library.findShortestPath

class Puzzle15 : AbstractAocDay(2021, 15) {

    override fun doA(file: String): String {
        val grid = readSingleLineFile(file).map { line -> line.map { it.digitToInt() } }

        val start = Coord(0, 0)
        val end = Coord(grid.size - 1, grid[0].size - 1)

        val result = findShortestPath(
            start,
            end,
            { coord -> Direction.CARDINAL_DIRECTIONS.map { coord.relative(it) }.filter { (row, column) -> row in grid.indices && column in grid[row].indices } },
            { _, (newRow, newColumn) -> grid[newRow][newColumn] }
        )
        return result.getScore().toString()
    }

    override fun doB(file: String): String {
        val grid = readSingleLineFile(file).map { line -> line.map { it.digitToInt() } }
        val verticalExpandedGrid = expandGrid(grid)

        val start = Coord(0, 0)
        val end = Coord(verticalExpandedGrid.size - 1, verticalExpandedGrid[0].size - 1)

        val result = findShortestPath(
            start,
            end,
            { coord -> Direction.CARDINAL_DIRECTIONS.map { coord.relative(it) }.filter { (row, column) -> row in verticalExpandedGrid.indices && column in verticalExpandedGrid[row].indices } },
            { _, (newRow, newColumn) -> verticalExpandedGrid[newRow][newColumn] }
        )
        return result.getScore().toString()
    }

    private fun expandGrid(grid: List<List<Int>>): List<List<Int>> {
        val horizontalExpandedGrid = grid
            .map { line ->
                val newList = mutableListOf<Int>()
                for (i in 0..4) {
                    newList += line.map {
                        val newValue = it + i
                        if (newValue > 9) newValue - 9 else newValue
                    }
                }
                newList
            }
        //        horizontalExpandedGrid.forEach { println(it.joinToString("")) }

        val verticalExpandedGrid = mutableListOf<List<Int>>()
        for (i in 0..4) {
            for (line in horizontalExpandedGrid) {
                val newLine = line.map {
                    val newValue = it + i
                    if (newValue > 9) newValue - 9 else newValue
                }
                verticalExpandedGrid.add(newLine)
            }
        }
        //        verticalExpandedGrid.forEach { println(it.joinToString("")) }
        return verticalExpandedGrid
    }

}