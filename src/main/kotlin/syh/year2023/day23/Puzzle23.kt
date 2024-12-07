package syh.year2023.day23

import syh.readSingleLineFile

data class Node(
    val x: Int, val y: Int, var type: String, var distance: Int = Int.MAX_VALUE
) {
    override fun toString(): String {
        return "[$x][$y], distance $distance"
    }

    fun toCoordString(): String {
        return "[$x][$y]"
    }
}

typealias Graph = MutableList<MutableList<Node>>

fun main() {
    val lines = readSingleLineFile("year2023/day23/actual.txt").map { it.split("").filter { c -> c.isNotEmpty() } }

    val graph = readGraph(lines)

    val startNode = graph.flatten().first { it.type == "S" }
    val endNode = graph.flatten().first { it.type == "E" }

    val junctionGraph = makeJunctionGraph(graph)

    val maxPath = dfsOptimized(junctionGraph, startNode, endNode, mutableMapOf())
//    val maxPath = depthFirstSearch(startNode, mutableListOf(startNode), graph, endNode)

//    maxPath.forEach { println(it) }
    println("max length is ${maxPath}")

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

private fun findNeighbours(source: Node, graph: Graph): List<Node> {
    val neighbours = mutableListOf<Node>()

//    if (source.type == "^") {
//        neighbours.add(graph[source.x - 1][source.y])
//    } else if (source.type == "v") {
//        neighbours.add(graph[source.x + 1][source.y])
//    } else if (source.type == ">") {
//        neighbours.add(graph[source.x][source.y + 1])
//    } else if (source.type == "<") {
//        neighbours.add(graph[source.x][source.y - 1])
//    } else {

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
//    }

    return neighbours
}

private fun isValidNeighbourValue(possibleNeighbour: Node): Boolean {
    return possibleNeighbour.type != "#"
}


private fun depthFirstSearch(currentNode: Node, path: MutableList<Node>, graph: Graph, endNode: Node): List<Node> {
    if (currentNode == endNode) {
        println("found ending with length ${path.size}")
        return path // here endeth the path
    }


    val neighbours = findNeighbours(currentNode, graph)

    var maxPath = mutableListOf<Node>().apply { addAll(path) }

    for (neighbour in neighbours) {
        if (neighbour in path) {
            println("neighbour ${neighbour.toCoordString()} is already in path")
            continue
        }

        println("checking neighbour ${neighbour.toCoordString()}")
        path.add(neighbour)
        val result = depthFirstSearch(neighbour, path, graph, endNode)
        if (result.contains(endNode) && result.size > maxPath.size) {
            println("found new max path ${result.size}")
            maxPath = mutableListOf<Node>().apply { addAll(result) }
        }
        path.remove(neighbour)

    }

    return maxPath
}


private fun makeJunctionGraph(graph: Graph): MutableMap<Node, MutableMap<Node, Int>> {
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






