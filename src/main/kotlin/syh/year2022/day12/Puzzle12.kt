package syh.year2022.day12

import syh.AbstractAocDay
import syh.library.Coord
import syh.library.Direction
import syh.library.Graph


class Puzzle12 : AbstractAocDay(2022, 12) {

    private val START_VALUE = 0
    private val END_VALUE = 27

    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)
            .map { line -> line.split("").filter { it.isNotEmpty() } }
        val graph = readGraph(lines)
        println("input has size ${lines.size} * ${lines[0].size} = ${lines.size * lines[0].size}")
        println("graph has ${graph.nodes.size} nodes")

        val start = graph.findNodesSatisfying { it.second == START_VALUE }.first()
        val end = graph.findNodesSatisfying { it.second == END_VALUE }.first()
        graph.dijkstra(start)
        println("path a steps: ${graph.getNode(end)}")
        return graph.getNode(end)!!.distance.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)
            .map { line -> line.split("").filter { it.isNotEmpty() } }
        val graph = readGraph(lines)

        val startPlaces = graph.findNodesSatisfying { it.second == START_VALUE || it.second == 1 }
        val end = graph.findNodesSatisfying { it.second == END_VALUE }.first()

        val minimumSteps = startPlaces.minOf { startB ->
            graph.dijkstra(startB)
            val endNode = graph.getNode(end)
            val steps = endNode!!.distance
            println("Start [${startB} took $steps steps")
            graph.reset()
            steps
        }

        println(minimumSteps)
        return minimumSteps.toString()
    }

    private fun readGraph(input: List<List<String>>): Graph<Pair<Coord, Int>> {
        val graph = Graph<Pair<Coord, Int>>()
        for (row in input.indices) {
            for (column in input[row].indices) {
                val coord = Coord(row, column)
                val node = coord to calculateValue(coord, input)

                for (direction in Direction.CARDINAL_DIRECTIONS) {
                    val newCoord = coord.relative(direction)
                    if (isValidNeighbourValue(coord, newCoord, input)) {
                        val neighbour = newCoord to calculateValue(newCoord, input)
                        graph.addEdge(node, neighbour, 1)
                    }
                }
            }
        }
        return graph
    }

    private fun isValidNeighbourValue(origin: Coord, new: Coord, grid: List<List<String>>): Boolean {
        if (new.row !in grid.indices || new.column !in grid[0].indices) {
            return false
        }
        val originValue = calculateValue(origin, grid)
        val neighbourValue = calculateValue(new, grid)
        return neighbourValue <= originValue + 1
    }

    private fun calculateValue(coord: Coord, grid: List<List<String>>): Int {
        val str = grid[coord.row][coord.column]
        if (str == "S") return START_VALUE
        if (str == "E") return END_VALUE
        return str[0].code - 96
    }
}
