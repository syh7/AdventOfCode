package syh.year2024

import syh.library.AbstractAocDay
import syh.library.Coord
import syh.library.Direction
import syh.library.Grid

class Puzzle20 : AbstractAocDay(2024, 20) {
    override fun doA(file: String): String {
        val shortcutMinimum = if (file == "test") 20 else 100
        val input = readSingleLineFile(file).map { it.split("").filter { c -> c.isNotEmpty() } }

        val grid = Grid<String>()
        grid.fill(input) { s: String -> s }

        val startCoord = grid.locationOf("S")
        val endCoord = grid.locationOf("E")

        println("start is ${startCoord.toCoordString()}")
        println("end is ${endCoord.toCoordString()}")

        grid.floodFill(startCoord, ::findNeighbours, ::neighbourTest)

        return countShortcuts(grid, 2, shortcutMinimum).toString()
    }

    override fun doB(file: String): String {
        val shortcutMinimum = if (file == "test") 20 else 100
        val input = readSingleLineFile(file).map { it.split("").filter { c -> c.isNotEmpty() } }

        val grid = Grid<String>()
        grid.fill(input) { s: String -> s }

        val startCoord = grid.locationOf("S")
        val endCoord = grid.locationOf("E")

        println("start is ${startCoord.toCoordString()}")
        println("end is ${endCoord.toCoordString()}")

        grid.floodFill(startCoord, ::findNeighbours, ::neighbourTest)

        return countShortcuts(grid, 20, shortcutMinimum).toString()
    }

    private fun countShortcuts(grid: Grid<String>, manhattanDistance: Int, shortcutMinimum: Int): Int {
        val possiblePathNodes = grid.grid.flatten().filter { it.distance != Long.MAX_VALUE }

        val totalShortcuts = possiblePathNodes.map { startNode ->
            possiblePathNodes.filter { it.value.first.manhattanDistance(startNode.value.first) <= manhattanDistance }
                .filter {
                    // if distance between two nodes is bigger than the manhattan + required shortcut distance, then it's a good shortcut
                    // do not calculate absolute distance change to prevent double counting shortcuts (left to right and back)
                    val distanceImprovement = it.distance - startNode.distance
                    distanceImprovement >= shortcutMinimum + it.value.first.manhattanDistance(startNode.value.first)
                }
        }.flatten().count()
        return totalShortcuts
    }

    private fun findNeighbours(coord: Coord) = Direction.CARDINAL_DIRECTIONS.map { coord.relative(it) }
    private fun neighbourTest(s: String) = s != "#"
}