package syh.year2024

import java.util.Stack
import java.util.function.Function
import syh.library.AbstractAocDay
import syh.library.Coord
import syh.library.Direction
import syh.library.Node

class Puzzle20 : AbstractAocDay(2024, 20) {
    override fun doA(file: String): String {
        val shortcutMinimum = if (file == "test") 20 else 100
        val input = readSingleLineFile(file).map { it.split("").filter { c -> c.isNotEmpty() } }

        val grid = SpecialGrid<String>()
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

        val grid = SpecialGrid<String>()
        grid.fill(input) { s: String -> s }

        val startCoord = grid.locationOf("S")
        val endCoord = grid.locationOf("E")

        println("start is ${startCoord.toCoordString()}")
        println("end is ${endCoord.toCoordString()}")

        grid.floodFill(startCoord, ::findNeighbours, ::neighbourTest)

        return countShortcuts(grid, 20, shortcutMinimum).toString()
    }

    private fun countShortcuts(grid: SpecialGrid<String>, manhattanDistance: Int, shortcutMinimum: Int): Int {
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

class SpecialGrid<T> {

    val grid = mutableListOf<MutableList<Node<Pair<Coord, T>>>>()

    fun locationOf(t: T): Coord {
        for (row in grid.indices) {
            for (column in grid[0].indices) {
                if (grid[row][column].value.second == t) {
                    return Coord(row, column)
                }
            }
        }
        throw IllegalStateException("value $t is not in grid")
    }

    fun fill(input: List<List<String>>, converter: Function<String, T>) {
        grid.clear()
        for (rowIndex in input.indices) {
            val row = mutableListOf<Node<Pair<Coord, T>>>()
            for (columnIndex in input[0].indices) {
                row.add(Node(Coord(rowIndex, columnIndex) to converter.apply(input[rowIndex][columnIndex])))
            }
            grid.add(row)
        }
    }

    fun floodFill(start: Coord, findNeighbours: Function<Coord, List<Coord>>, neighbourTest: Function<T, Boolean>) {
        val visited = mutableSetOf<Node<Pair<Coord, T>>>()

        val startNode = at(start)
        startNode.distance = 0

        val stack = Stack<Node<Pair<Coord, T>>>()
        stack.add(startNode)

        while (stack.isNotEmpty()) {
            val node = stack.pop()
            if (visited.contains(node)) {
                continue
            }

            visited.add(node)
            val newDistance = node.distance + 1

            val neighbours = findNeighbours.apply(node.value.first)
//            println("checking ${node.coord} with distance ${node.distance} and neighbours ${neighbours.map { it.toCoordString() }}")
            for (neighbour in neighbours) {
                val neighbourNode = at(neighbour)
                if (neighbourTest.apply(neighbourNode.value.second)) {
                    if (neighbourNode.distance > newDistance) {
                        neighbourNode.predecessors.clear()
                        neighbourNode.predecessors.add(node)
                        neighbourNode.distance = newDistance
                    }
                    if (newDistance == neighbourNode.distance) {
                        neighbourNode.predecessors.add(node)
                    }
                    stack.add(neighbourNode)
                }
            }
        }
    }

    fun at(coord: Coord): Node<Pair<Coord, T>> {
        return grid[coord.row][coord.column]
    }

}