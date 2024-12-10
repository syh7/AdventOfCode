package syh.year2024.day10

import syh.AbstractAocDay


typealias Graph = MutableList<MutableList<Puzzle10.Node>>

class Puzzle10 : AbstractAocDay(2024, 10) {
    override fun doA(file: String): Long {
        println("reading file $file")
        val graph = readSingleLineFile(file)
            .map { it.split("").filter { c -> c.isNotEmpty() } }
            .let { readGraph(it) }

        val startNodes = graph.flatten().filter { it.height.toIntOrNull() == 0 }
        println("found startnodes:")
        startNodes.forEach { println(it) }

        val totalTrails = startNodes.associateWith { walkToEnding(it, graph) }
        println("found trails:")
        totalTrails.forEach { (start, endings) ->
            println("\tfor start $start found ${endings.size} endings")
            println(endings)
        }
        println()
        println()
        return totalTrails.map { it.value.size }.sum().toLong()
    }

    override fun doB(file: String): Long {
        println("reading file $file")
        val graph = readSingleLineFile(file)
            .map { it.split("").filter { c -> c.isNotEmpty() } }
            .let { readGraph(it) }

        val startNodes = graph.flatten().filter { it.height.toIntOrNull() == 0 }
        println("found startnodes:")
        startNodes.forEach { println(it) }

        val totalTrails = startNodes.associateWith { walkDistinctPath(it, graph) }
        println("found trails:")
        totalTrails.forEach { (start, endings) ->
            println("\tfor start $start found $endings endings")
        }
        println()
        println()
        return totalTrails.map { it.value }.sum().toLong()
    }

    private fun walkToEnding(currentNode: Node, graph: Graph): Set<Node> {
        println("walking node $currentNode")
        if (currentNode.height.toIntOrNull() == 9) {
            return setOf(currentNode)
        }

        val reachableNeighbours = reachableNeighbours(currentNode, graph)
        reachableNeighbours.forEach { println("found neighbour $it") }

        return reachableNeighbours
            .map { walkToEnding(it, graph) }
            .flatten()
            .toSet()

    }

    private fun walkDistinctPath(currentNode: Node, graph: Graph): Int {
        println("walking node $currentNode")
        if (currentNode.height.toIntOrNull() == 9) {
            return 1
        }

        val reachableNeighbours = reachableNeighbours(currentNode, graph)
        reachableNeighbours.forEach { println("found neighbour $it") }

        return reachableNeighbours
            .sumOf { walkDistinctPath(it, graph) }

    }

    private fun reachableNeighbours(currentNode: Node, graph: MutableList<MutableList<Node>>): List<Node> {
        val neighbours = mutableListOf<Node>()

        if (currentNode.x - 1 in graph[0].indices && isNeighbourValue(graph[currentNode.y][currentNode.x - 1], currentNode)) {
            neighbours.add(graph[currentNode.y][currentNode.x - 1])
        }
        if (currentNode.x + 1 in graph[0].indices && isNeighbourValue(graph[currentNode.y][currentNode.x + 1], currentNode)) {
            neighbours.add(graph[currentNode.y][currentNode.x + 1])
        }
        if (currentNode.y - 1 in graph.indices && isNeighbourValue(graph[currentNode.y - 1][currentNode.x], currentNode)) {
            neighbours.add(graph[currentNode.y - 1][currentNode.x])
        }
        if (currentNode.y + 1 in graph.indices && isNeighbourValue(graph[currentNode.y + 1][currentNode.x], currentNode)) {
            neighbours.add(graph[currentNode.y + 1][currentNode.x])
        }
        return neighbours
    }

    private fun isNeighbourValue(optionalNeighbour: Node, currentNode: Node) = (optionalNeighbour.height.toIntOrNull() != null
            && optionalNeighbour.height.toIntOrNull() == currentNode.height.toIntOrNull()!! + 1)

    private fun readGraph(input: List<List<String>>): Graph {
        val allNodes: Graph = mutableListOf()
        for (i in input.indices) {
            val rowNodes = mutableListOf<Node>()
            for (j in input[i].indices) {
                rowNodes.add(Node(x = j, y = i, height = input[j][i]))
            }
            allNodes.add(rowNodes)
        }
        return allNodes
    }

    data class Node(val x: Int, val y: Int, var height: String) {
        override fun toString(): String {
            return "[$x][$y]($height)"
        }
    }

}