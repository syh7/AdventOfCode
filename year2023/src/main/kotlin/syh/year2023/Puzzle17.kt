package syh.year2023

import syh.library.AbstractAocDay
import syh.library.Coord
import syh.library.Direction
import syh.library.findShortestPathByPredicate


class Puzzle17 : AbstractAocDay(2023, 17) {

    data class CoordWithDirectionAndLength(
        val coord: Coord,
        val direction: Direction,
        val length: Int
    )

    override fun doA(file: String): String {
        val map = readSingleLineFile(file).map { line -> line.map { it.digitToInt() } }

        val start = CoordWithDirectionAndLength(Coord(0, 0), Direction.EAST, 0)
        val endCoord = Coord(map.last().lastIndex, map.lastIndex)

        val path = findShortestPathByPredicate(
            start,
            { (p, _) -> p == endCoord },
            { findNeighbours(it, 0, 3, map) },
            { _, (point) -> map[point.row][point.column] })
        return path.getScore().toString()
    }

    override fun doB(file: String): String {
        val map = readSingleLineFile(file).map { line -> line.map { it.digitToInt() } }

        val start = CoordWithDirectionAndLength(Coord(0, 0), Direction.EAST, 0)
        val endCoord = Coord(map.last().lastIndex, map.lastIndex)

        val path = findShortestPathByPredicate(
            start,
            { (p, _, length) -> p == endCoord && length >= 4 },
            { findNeighbours(it, 4, 10, map) },
            { _, (point) -> map[point.row][point.column] })
        return path.getScore().toString()
    }

    private fun findNeighbours(start: CoordWithDirectionAndLength, minimumLength: Int, maximumLength: Int, map: List<List<Int>>): List<CoordWithDirectionAndLength> {
        val straight = start.direction

        val neighbours = mutableListOf<CoordWithDirectionAndLength>()
        if (start.length < maximumLength) {
            neighbours.add(CoordWithDirectionAndLength(start.coord.relative(straight), straight, start.length + 1))
        }
        if (start.length >= minimumLength || start.length == 0) {
            neighbours.add(CoordWithDirectionAndLength(start.coord.relative(straight.right90()), straight.right90(), 1))
            neighbours.add(CoordWithDirectionAndLength(start.coord.relative(straight.left90()), straight.left90(), 1))
        }

        return neighbours.filter { neighbour -> neighbour.coord.row in map.indices && neighbour.coord.column in map[0].indices }
    }
}