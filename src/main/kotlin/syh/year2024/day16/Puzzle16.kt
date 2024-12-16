package syh.year2024.day16

import syh.AbstractAocDay
import syh.library.Coord
import syh.library.Direction
import syh.library.Graph
import kotlin.math.min


class Puzzle16 : AbstractAocDay(2024, 16) {
    override fun doA(file: String): String {
        val lines = readSingleLineFile(file).map { it.split("").filter { c -> c.isNotEmpty() } }

        val graph = Graph<Pair<Coord, Direction>>()

        val startPositions = mutableListOf<Pair<Coord, Direction>>()
        val endPositions = mutableListOf<Pair<Coord, Direction>>()

        for (row in lines.indices) {
            for (column in lines[row].indices) {
                // skip walls
                if (lines[row][column] == "#") continue

                if (lines[row][column] == "S") {
                    Direction.CARDINAL_DIRECTIONS.forEach { startPositions.add(Coord(row, column) to it) }
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
                        graph.addEdge(current, leftCoord to leftDir, 1000)
                    }

                    val rightDir = dir.right90()
                    val rightCoord = coord.relative(rightDir)
                    if (validNeighbour(lines, rightCoord)) {
                        graph.addEdge(current, rightCoord to rightDir, 1000)
                    }
                }
            }
        }

        println("found start positions:")
        startPositions.forEach { println(it) }
        println("found end positions:")
        endPositions.forEach { println(it) }

        var minimum = Long.MAX_VALUE

        for (start in startPositions) {
            println("checking start $start")
            val shortestPath = graph.dijkstra(start)
            println("shortest path: ")
            println(shortestPath)
            for (end in endPositions) {
                shortestPath.filter { it.value == end }.forEach {
                    println("found path of ${shortestPath.size} nodes from $start to $end with distance ${it.distance}")
                    minimum = min(minimum, it.distance)
                }
            }
        }

        println("absolute minimum is $minimum")
        return minimum.toString()
    }

    override fun doB(file: String): String {
        return ""
    }

    private fun validNeighbour(lines: List<List<String>>, coord: Coord): Boolean {
        return coord.row in lines.indices && coord.column in lines[coord.row].indices && lines[coord.row][coord.column] != "#"
    }

}