package syh.library

import java.util.function.Function

class Grid<T : Any> {

    val grid = mutableListOf<MutableList<Pair<Coord, T>>>()

    fun create(input: List<List<String>>, converter: Function<String, T>) {
        grid.clear()
        for (rowIndex in input.indices) {
            val row = mutableListOf<Pair<Coord, T>>()
            for (columnIndex in input[0].indices) {
                row.add(Coord(rowIndex, columnIndex) to converter.apply(input[rowIndex][columnIndex]))
            }
            grid.add(row)
        }
    }

    fun locationOf(t: T): List<Pair<Coord, T>> {
        return grid.flatten().filter { it.second == t }
    }

    fun findNeighboursWithValues(startCoord: Coord): List<Pair<Coord, T>> {
        val neighbours = mutableListOf<Pair<Coord, T>>()
        for (columnOffset in -1..1) {
            for (rowOffset in -1..1) {
                if (rowOffset == 0 && columnOffset == 0) {
                    continue
                }
                val newRow = startCoord.row + rowOffset
                val newColumn = startCoord.column + columnOffset
                if (newRow in grid.indices && newColumn in grid[newRow].indices) {
                    neighbours.add(grid[newRow][newColumn])
                }
            }
        }
        return neighbours
    }

    fun findNeighbours(startCoord: Coord, directions: List<Direction>): List<Pair<Coord, T>> {
        return directions.map { startCoord.relative(it) }
            .filter { (row, column) -> row in grid.indices && column in grid[row].indices }
            .map { it to at(it) }
    }

    fun at(coord: Coord): T {
        return grid[coord.row][coord.column].second
    }

    fun set(coord: Coord, newValue: T) {
        grid[coord.row][coord.column] = coord to newValue
    }

    fun flatten(): List<Pair<Coord, T>> {
        return grid.flatten()
    }

    fun printValues() {
        for (row in grid) {
            for (cell in row) {
                print(cell.second)
            }
            println()
        }
    }

}