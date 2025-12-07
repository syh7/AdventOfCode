package syh.year2024

import syh.library.AbstractAocDay
import syh.library.Coord
import syh.library.Direction
import syh.library.Grid


class Puzzle10 : AbstractAocDay(2024, 10) {
    override fun doA(file: String): String {
        val input = readSingleLineFile(file).map { it.split("").filter { c -> c.isNotEmpty() } }
        val grid = Grid<Int>()
        grid.create(input) { if (it == ".") -1 else it.toInt() }
        grid.printValues()

        val startNodes = grid.locationOf(0)
        println("found startnodes:")
        startNodes.forEach { println(it) }

        val totalTrails = startNodes.associateWith { walkToEnding(it, grid) }
        println("found trails:")
        totalTrails.forEach { (start, endings) ->
            println("\tfor start $start found ${endings.size} endings")
            println(endings)
        }
        println()
        println()
        return totalTrails.map { it.value.size }.sum().toString()
    }

    override fun doB(file: String): String {
        val input = readSingleLineFile(file).map { it.split("").filter { c -> c.isNotEmpty() } }
        val grid = Grid<Int>()
        grid.create(input) { if (it == ".") -1 else it.toInt() }
        grid.printValues()

        val startNodes = grid.locationOf(0)
        println("found startnodes:")
        startNodes.forEach { println(it) }

        val totalTrails = startNodes.associateWith { walkDistinctPath(it, grid) }
        println("found trails:")
        totalTrails.forEach { (start, endings) ->
            println("\tfor start $start found $endings endings")
        }
        println()
        println()
        return totalTrails.map { it.value }.sum().toString()
    }

    private fun walkToEnding(currentNode: Pair<Coord, Int>, grid: Grid<Int>): Set<Pair<Coord, Int>> {
        println("walking node $currentNode")
        if (currentNode.second == 9) {
            return setOf(currentNode)
        }

        val reachableNeighbours = grid.findNeighbours(currentNode.first, Direction.CARDINAL_DIRECTIONS)
            .filter { isNeighbourValue(it, currentNode) }
        reachableNeighbours.forEach { println("found neighbour $it") }

        return reachableNeighbours
            .map { walkToEnding(it, grid) }
            .flatten()
            .toSet()
    }

    private fun walkDistinctPath(currentNode: Pair<Coord, Int>, graph: Grid<Int>): Int {
        println("walking node $currentNode")
        if (currentNode.second == 9) {
            return 1
        }

        val reachableNeighbours = graph.findNeighbours(currentNode.first, Direction.CARDINAL_DIRECTIONS)
            .filter { isNeighbourValue(it, currentNode) }
        reachableNeighbours.forEach { println("found neighbour $it") }

        return reachableNeighbours
            .sumOf { walkDistinctPath(it, graph) }
    }

    private fun isNeighbourValue(optionalNeighbour: Pair<Coord, Int>, currentNode: Pair<Coord, Int>): Boolean {
        return optionalNeighbour.second != -1 && optionalNeighbour.second == currentNode.second + 1
    }
}