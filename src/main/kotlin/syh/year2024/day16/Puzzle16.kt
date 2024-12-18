package syh.year2024.day16

import syh.AbstractAocDay
import syh.library.Coord
import syh.library.Direction
import syh.library.Graph
import syh.library.Node
import java.util.*
import kotlin.math.min


class Puzzle16 : AbstractAocDay(2024, 16) {
    override fun doA(file: String): String {
        val lines = readSingleLineFile(file).map { it.split("").filter { c -> c.isNotEmpty() } }

        val (graph, startPositions, endPositions) = readGraphWithStartAndEndPositions(lines)
        val start = startPositions[0]

        var minimum = Long.MAX_VALUE

        println("checking start $start")
        val visitedNodes = graph.dijkstra(start)
//        println("visited nodes: ")
//        println(visitedNodes)

        for (end in endPositions) {
            visitedNodes.filter { it.value == end }.forEach {
                println("found path of ${visitedNodes.size} nodes from $start to $end with distance ${it.distance}")
                minimum = min(minimum, it.distance)
            }
        }

        println("absolute minimum is $minimum")
        return minimum.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file).map { it.split("").filter { c -> c.isNotEmpty() } }

        val (graph, startPositions, endPositions) = readGraphWithStartAndEndPositions(lines)

        val start = startPositions[0]
        val endCoord = endPositions[0].first

        println("checking start $start")
        val visitedNodes = graph.dijkstra(start)
//        println("visited nodes: ")
//        println(visitedNodes)

        val bestPathTiles = mutableSetOf<Coord>()
        val stack = Stack<Node<Pair<Coord, Direction>>>()

        var minimum = Long.MAX_VALUE

        for (end in endPositions) {
            visitedNodes.filter { it.value == end }.forEach {
                println("found path of ${visitedNodes.size} nodes from $start to $end with distance ${it.distance}")
                minimum = min(minimum, it.distance)
            }
        }

        visitedNodes.filter { it.value.first == endCoord && it.distance == minimum }.forEach { stack.push(it) }

        while (stack.isNotEmpty()) {
            val node = stack.pop()
            bestPathTiles.add(node.value.first)
            println("counting best tile $node")
            node.predecessors.forEach {
                if (!stack.contains(it)) stack.push(it)
            }
        }

        println("total best path tiles: ${bestPathTiles.size}")
        println(bestPathTiles)
        return bestPathTiles.size.toString()
    }

    private fun readGraphWithStartAndEndPositions(lines: List<List<String>>): Triple<Graph<Pair<Coord, Direction>>, MutableList<Pair<Coord, Direction>>, MutableList<Pair<Coord, Direction>>> {
        val graph = Graph<Pair<Coord, Direction>>()

        val startPositions = mutableListOf<Pair<Coord, Direction>>()
        val endPositions = mutableListOf<Pair<Coord, Direction>>()

        for (row in lines.indices) {
            for (column in lines[row].indices) {
                // skip walls
                if (lines[row][column] == "#") continue

                if (lines[row][column] == "S") {
                    startPositions.add(Coord(row, column) to Direction.EAST)
                }
                if (lines[row][column] == "E") {
                    Direction.CARDINAL_DIRECTIONS.forEach { endPositions.add(Coord(row, column) to it) }
                }

                for (dir in Direction.CARDINAL_DIRECTIONS) {
                    val coord = Coord(row, column)
                    val current = Pair(coord, dir)

                    val straight = coord.relative(dir)
                    if (validNeighbour(lines, straight)) {
                        graph.addEdge(current, straight to dir, 1)
                    }

                    val leftDir = dir.left90()
                    val leftCoord = coord.relative(leftDir)
                    if (validNeighbour(lines, leftCoord)) {
                        graph.addEdge(current, leftCoord to leftDir, 1001)
                    }

                    val rightDir = dir.right90()
                    val rightCoord = coord.relative(rightDir)
                    if (validNeighbour(lines, rightCoord)) {
                        graph.addEdge(current, rightCoord to rightDir, 1001)
                    }
                }
            }
        }

        println("found start positions:")
        startPositions.forEach { println(it) }
        println("found end positions:")
        endPositions.forEach { println(it) }
        return Triple(graph, startPositions, endPositions)
    }

    private fun validNeighbour(lines: List<List<String>>, coord: Coord): Boolean {
        return coord.row in lines.indices && coord.column in lines[coord.row].indices && lines[coord.row][coord.column] != "#"
    }

}