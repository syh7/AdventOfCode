package syh.year2023.day23

import syh.AbstractAocDay


typealias Graph = MutableList<MutableList<Puzzle23.Node>>

class Puzzle23 : AbstractAocDay(2023, 23) {
    override fun doA(file: String): Long {
        val lines = readSingleLineFile(file)
            .map { it.split("").filter { c -> c.isNotEmpty() } }

        val graph = readGraph(lines)

        val startNode = graph.flatten().first { it.type == "S" }
        val endNode = graph.flatten().first { it.type == "E" }

        val junctionGraph = makeJunctionGraph(graph, ::findNeighboursA)

        val maxPath = dfsOptimized(junctionGraph, startNode, endNode, mutableMapOf())!!

        println("max length is $maxPath")
        return maxPath.toLong()
    }

    override fun doB(file: String): Long {
        val lines = readSingleLineFile(file)
            .map { it.split("").filter { c -> c.isNotEmpty() } }

        val graph = readGraph(lines)

        val startNode = graph.flatten().first { it.type == "S" }
        val endNode = graph.flatten().first { it.type == "E" }

        val junctionGraph = makeJunctionGraph(graph, ::findNeighboursB)

        val maxPath = dfsOptimized(junctionGraph, startNode, endNode, mutableMapOf())!!

        println("max length is $maxPath")
        return maxPath.toLong()
    }

    data class Node(
        val x: Int, val y: Int, var type: String, var distance: Int = Int.MAX_VALUE
    ) {
        override fun toString(): String {
            return "[$x][$y], distance $distance"
        }
    }

    private fun readGraph(input: List<List<String>>): Graph {
        val allNodes: Graph = mutableListOf()
        for (i in input.indices) {
            val rowNodes = mutableListOf<Node>()
            for (j in input[i].indices) {
                rowNodes.add(Node(x = i, y = j, type = input[i][j]))
            }
            allNodes.add(rowNodes)
        }
        return allNodes
    }

    private fun findNeighboursA(source: Node, graph: Graph): List<Node> {
        val neighbours = mutableListOf<Node>()

        when (source.type) {
            "^" -> neighbours.add(graph[source.x - 1][source.y])
            "v" -> neighbours.add(graph[source.x + 1][source.y])
            ">" -> neighbours.add(graph[source.x][source.y + 1])
            "<" -> neighbours.add(graph[source.x][source.y - 1])
            else -> {
                neighbours.addAll(findNeighboursB(source, graph))
            }
        }

        return neighbours
    }

    private fun findNeighboursB(source: Node, graph: Graph): List<Node> {
        val neighbours = mutableListOf<Node>()

        if (source.x + 1 in graph.indices && isValidNeighbourValue(graph[source.x + 1][source.y])) {
            neighbours.add(graph[source.x + 1][source.y])
        }
        if (source.x - 1 in graph.indices && isValidNeighbourValue(graph[source.x - 1][source.y])) {
            neighbours.add(graph[source.x - 1][source.y])
        }
        if (source.y + 1 in graph[source.x].indices && isValidNeighbourValue(graph[source.x][source.y + 1])) {
            neighbours.add(graph[source.x][source.y + 1])
        }
        if (source.y - 1 in graph[source.x].indices && isValidNeighbourValue(graph[source.x][source.y - 1])) {
            neighbours.add(graph[source.x][source.y - 1])
        }

        return neighbours
    }

    private fun isValidNeighbourValue(possibleNeighbour: Node): Boolean {
        return possibleNeighbour.type != "#"
    }


    private fun makeJunctionGraph(graph: Graph, findNeighbours: (Node, Graph) -> List<Node>): MutableMap<Node, MutableMap<Node, Int>> {
        val adjacencies = graph.indices.flatMap { x ->
            graph[0].indices.mapNotNull { y ->
                if (graph[x][y].type != "#") {
                    val neighbours = findNeighbours(graph[x][y], graph)
                    graph[x][y] to neighbours.associateWith { 1 }.toMutableMap()
                } else null
            }
        }.toMap(mutableMapOf())

        adjacencies.keys.toList().forEach { key ->
            adjacencies[key]?.takeIf { it.size == 2 }?.let { neighbors ->
                val left = neighbors.keys.first()
                val right = neighbors.keys.last()
                val totalSteps = neighbors[left]!! + neighbors[right]!!
                adjacencies.getOrPut(left) { mutableMapOf() }.merge(right, totalSteps, ::maxOf)
                adjacencies.getOrPut(right) { mutableMapOf() }.merge(left, totalSteps, ::maxOf)
                listOf(left, right).forEach { adjacencies[it]?.remove(key) }
                adjacencies.remove(key)
            }
        }
        return adjacencies
    }

    private fun dfsOptimized(graph: Map<Node, Map<Node, Int>>, cur: Node, end: Node, seen: MutableMap<Node, Int>): Int? {
        if (cur == end) {
            return seen.values.sum()
        }

        var best: Int? = null
        (graph[cur] ?: emptyMap()).forEach { (neighbor, steps) ->
            if (neighbor !in seen) {
                seen[neighbor] = steps
                val res = dfsOptimized(graph, neighbor, end, seen)
                if (best == null || (res != null && res > best!!)) {
                    best = res
                }
                seen.remove(neighbor)
            }
        }
        return best
    }
}
