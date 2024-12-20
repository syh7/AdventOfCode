package syh.year2024.day20

import syh.AbstractAocDay
import syh.library.Coord
import syh.library.Direction
import syh.library.Graph


class Puzzle20 : AbstractAocDay(2024, 20) {
    override fun doA(file: String): String {
        val grid = readSingleLineFile(file).map { it.split("").filter { c -> c.isNotEmpty() } }

        val (graph, walls) = readGraphAndWalls(grid)

        val startCoord = graph.findNodesSatisfying { pair -> pair.second == "S" }[0]
        val endCoord = graph.findNodesSatisfying { pair -> pair.second == "E" }[0]

        graph.dijkstra(startCoord)

        var endNode = graph.getNode(endCoord)!!
        println("original path")
        println(endNode)
        println(endNode.getPredecessors().map { it.value.first.toCoordString() })
        println()
        val originalDistance = endNode.distance


        val possibleShortscuts = mutableSetOf<Coord>()
        for (wall in walls) {
            for (direction in Direction.CARDINAL_DIRECTIONS) {
                val opposite = direction.opposite()
//                println("direction $direction has opposite $opposite")
                if (isValidNeighbourValue(wall.relative(direction), grid) && isValidNeighbourValue(wall.relative(opposite), grid)) {
//                    println("wall $wall is shortcut")
                    possibleShortscuts.add(wall)
                }
            }
        }

        val shortcutDistanceMap = possibleShortscuts.associateWith { shortcut ->
//            println("checking shortcut ${shortcut.toCoordString()}")
            val neighbours = findNeighbours(shortcut, grid)
            graph.reset()
            neighbours.forEach {
                graph.addEdge(shortcut to grid.at(shortcut), it to grid.at(it), 0)
                graph.addEdge(it to grid.at(it), shortcut to grid.at(shortcut), 0)
            }
            graph.dijkstra(startCoord)

            endNode = graph.getNode(endCoord)!!
            val predecessors = endNode.getPredecessors()
//            println("new path")
//            println("$endNode with distance ${endNode.distance} with ${predecessors.size} predecessors:")
//            println(predecessors.map { it.value.first.toCoordString() })

            neighbours.forEach {
                graph.removeEdge(shortcut to grid.at(shortcut), it to grid.at(it))
                graph.removeEdge(it to grid.at(it), shortcut to grid.at(shortcut))
            }

            val newDistance = predecessors.size
            val saved = originalDistance - newDistance
//            println("${shortcut.toCoordString()} saved $saved")
            saved
        }.filter { it.value != originalDistance }

        shortcutDistanceMap.forEach { (shortcut, distance) -> println("shortcut at $shortcut has distance $distance") }

        val grouped = shortcutDistanceMap.values.groupingBy { it }.eachCount()
//        grouped.toSortedMap().forEach { (k, v) -> println("there are $v cheats that save $k picoseconds") }

        val minimumCheat = if (file == "test") 20 else 100
        val minimumCheats = grouped.filter { it.key >= minimumCheat }.values.sum()
        println("there are $minimumCheats ways to save at least $minimumCheat picoseconds")
        return minimumCheats.toString()
    }

    override fun doB(file: String): String {
        return ""
    }

    private fun readGraphAndWalls(grid: List<List<String>>): Pair<Graph<Pair<Coord, String>>, MutableList<Coord>> {
        val possibleShortcuts = mutableListOf<Coord>()

        val graph = Graph<Pair<Coord, String>>()
        for (row in grid.indices) {
            for (column in grid[0].indices) {
                if (row == 0 || column == 0 || row == grid.size - 1 || column == grid[0].size - 1) {
                    continue
                }

                if (grid[row][column] == "#") {
                    possibleShortcuts.add(Coord(row, column))
                    continue
                }

                val from = Coord(row, column) to grid[row][column]
                findNeighbours(from.first, grid).forEach {
                    //                    println("add edge from $from to $it")
                    graph.addEdge(from, it to grid.at(it), 1)
                }
            }
        }
        return graph to possibleShortcuts
    }

    private fun findNeighbours(source: Coord, grid: List<List<String>>): List<Coord> {
        val neighbours = mutableListOf<Coord>()

        for (direction in Direction.CARDINAL_DIRECTIONS) {
            val neighbourCoord = source.relative(direction)
            if (neighbourCoord.row in grid.indices && neighbourCoord.column in grid[0].indices && isValidNeighbourValue(neighbourCoord, grid)) {
                neighbours.add(neighbourCoord)
            }
        }

        return neighbours
    }

    private fun isValidNeighbourValue(coord: Coord, grid: List<List<String>>): Boolean {
        return grid.at(coord) != "#"
    }

    private fun List<List<String>>.at(coord: Coord): String {
        return this[coord.row][coord.column]
    }
}
