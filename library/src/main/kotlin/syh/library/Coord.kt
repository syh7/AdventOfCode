package syh.library

import kotlin.math.abs

data class Coord(val row: Int, val column: Int) {

    fun toCoordString() = "[$row][$column]"
    override fun toString(): String {
        return toCoordString()
    }

    fun relative(direction: Direction): Coord {
        return Coord(this.row + direction.row, this.column + direction.col)
    }

    fun manhattanDistance(other: Coord): Int {
        return abs(row - other.row) + abs(column - other.column)
    }

    fun mod(rowMod: Int, columnMod: Int): Coord {
        return Coord((row + rowMod) % rowMod, (column + columnMod) % columnMod)
    }

    fun isNeighbour(other: Coord, allowedNeighbourDirections: List<Direction>): Boolean {
        return allowedNeighbourDirections.any { this.relative(it) == other }
    }

    fun plus(other: Coord): Coord {
        return Coord(this.row + other.row, this.column + other.column)
    }
}