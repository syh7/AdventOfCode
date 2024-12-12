package syh.year2022.day12

import syh.AbstractAocDay


typealias Graph = MutableList<MutableList<Puzzle12.Node>>


class Puzzle12 : AbstractAocDay(2022, 12) {

    private val START_VALUE = 0
    private val END_VALUE = 27

    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)
            .map { line -> line.split("").filter { it.isNotEmpty() } }
        val graph = readGraph(lines)

        val startPartA = graph.flatten().first { it.value == START_VALUE }
        val pathA = performDijkstra(graph, startPartA)
        println("path a steps: " + pathA.maxBy { it.distance }.distance)
        return pathA.maxBy { it.distance }.distance.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)
            .map { line -> line.split("").filter { it.isNotEmpty() } }
        val graph = readGraph(lines)

        val startPartB = graph.flatten().filter { it.value == START_VALUE || it.value == 1 }
        val minimumStepsB = startPartB.minOf { startB ->
            val pathB = performDijkstra(graph, startB)
            val steps = pathB.maxByOrNull { it.distance }?.distance ?: Int.MAX_VALUE
            println("Start [${startB.x}][${startB.y}] took $steps steps")
            resetGraph(graph)
            steps
        }

        println(minimumStepsB)
        return minimumStepsB.toString()
    }

    private fun performDijkstra(graph: Graph, startNode: Node): MutableList<Node> {
        var unvisited = graph.flatten().toMutableList()

        startNode.distance = 0

        while (unvisited.isNotEmpty()) {
            unvisited = unvisited.sortedBy { it.distance }.toMutableList()

            val currentNode = unvisited.first()
            unvisited.remove(currentNode)

            val unvisitedNeighbours = findNeighbours(currentNode, graph).filter { unvisited.contains(it) }

            val newDistance = currentNode.distance + 1
            unvisitedNeighbours.forEach { neighbour ->
                if (neighbour.distance > newDistance) {
                    neighbour.distance = newDistance
                    neighbour.previous = currentNode
                }
            }
        }

        val endNode = graph.flatten().first { it.value == END_VALUE }

        val path = mutableListOf(endNode)

        var pathNode: Node? = endNode
        while (pathNode != null && pathNode != startNode) {
            pathNode = pathNode.previous
            pathNode?.let { path.add(it) }
        }
        return if (path.contains(startNode)) {
            path
        } else {
            println("did not find path for start [${startNode.x}][${startNode.y}]")
            mutableListOf()
        }
    }

    private fun readGraph(input: List<List<String>>): Graph {
        val allNodes: Graph = mutableListOf()
        for (i in input.indices) {
            val rowNodes = mutableListOf<Node>()
            for (j in input[i].indices) {
                rowNodes.add(Node(x = i, y = j, value = calculateValue(input[i][j])))
            }
            allNodes.add(rowNodes)
        }
        return allNodes
    }

    private fun resetGraph(graph: Graph) {
        graph.flatten().forEach {
            it.distance = Int.MAX_VALUE
            it.previous = null
        }
    }

    private fun calculateValue(str: String): Int {
        if (str == "S") return START_VALUE
        if (str == "E") return END_VALUE
        return str[START_VALUE].code - 96
    }

    private fun findNeighbours(source: Node, graph: Graph): List<Node> {
        val neighbours = mutableListOf<Node>()

        if (source.x + 1 in graph.indices && isValidNeighbourValue(source, graph[source.x + 1][source.y])) {
            neighbours.add(graph[source.x + 1][source.y])
        }
        if (source.x - 1 in graph.indices && isValidNeighbourValue(source, graph[source.x - 1][source.y])) {
            neighbours.add(graph[source.x - 1][source.y])
        }
        if (source.y + 1 in graph[source.x].indices && isValidNeighbourValue(source, graph[source.x][source.y + 1])) {
            neighbours.add(graph[source.x][source.y + 1])
        }
        if (source.y - 1 in graph[source.x].indices && isValidNeighbourValue(source, graph[source.x][source.y - 1])) {
            neighbours.add(graph[source.x][source.y - 1])
        }

        return neighbours.reversed()
    }

    private fun isValidNeighbourValue(source: Node, possibleNeighbour: Node): Boolean {
        return possibleNeighbour.value == source.value || possibleNeighbour.value == source.value + 1 || possibleNeighbour.value < source.value
    }

    data class Node(
        val x: Int, val y: Int, val value: Int, var previous: Node? = null, var distance: Int = Int.MAX_VALUE
    ) {
        override fun toString(): String {
            val previousStr =
                if (previous != null) "with previous [${previous!!.x}][${previous!!.y}]" else "without previous"
            return "[$x][$y], distance $distance, $previousStr"
        }
    }
}
