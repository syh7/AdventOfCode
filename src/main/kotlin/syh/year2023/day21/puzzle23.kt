package syh.year2023.day21

import syh.readSingleLineFile
import java.math.BigDecimal

data class Node(
    val x: Int, val y: Int, var type: String, var previous: Node? = null, var distance: Int = Int.MAX_VALUE
) {
    override fun toString(): String {
        val previousStr =
            if (previous != null) "with previous [${previous!!.x}][${previous!!.y}]" else "without previous"
        return "[$x][$y], distance $distance, $previousStr"
    }

    fun toDistanceStr(): String {
        if (type == "#" || distance == Int.MAX_VALUE) {
            return "[   ]"
        }
        return if (distance < 10) {
            "[  $distance]"
        } else if (distance < 100) {
            "[ $distance]"
        } else {
            "[$distance]"
        }
    }

    fun toPreviousDirectionStr(): String {
        if (type == "S") {
            return "[S]"
        }
        if (previous == null) {
            return "[ ]"
        }
        if (previous!!.x < x) {
            return "[^]"
        }
        if (previous!!.x > x) {
            return "[v]"
        }
        if (previous!!.y > y) {
            return "[>]"
        }
        if (previous!!.y < y) {
            return "[<]"
        }
        throw IllegalStateException("weird previous values for node $this")
    }

    fun toPreviousAndDistanceStr(): String {
        val distStr = toDistanceStr().removePrefix("[").removeSuffix("]")
        val dirStr = toPreviousDirectionStr().removePrefix("[").removeSuffix("]")
        return "[$distStr $dirStr]"
    }
}

typealias Graph = MutableList<MutableList<Node>>

fun main() {
    val lines = readSingleLineFile("year2023/day21/actual.txt").map { it.split("").filter { c -> c.isNotEmpty() } }

    val graph = readGraph(lines)

    val startNode = graph.flatten().first { it.type == "S" }

    performDijkstra(graph, startNode)

//    graph.forEach { line -> println(line.joinToString("") { it.toDistanceStr() }) }
//    graph.forEach { line -> println(line.joinToString("") { it.toPreviousDirectionStr() }) }
//    graph.forEach { line -> println(line.joinToString("") { it.toPreviousAndDistanceStr() }) }

    partA(graph)
    partB(graph)

}

private fun partA(graph: Graph) {
    val evenNodes = graph.flatten().filter { it.distance % 2 == 0 && it.distance <= 64 }

    evenNodes.forEach { it.type = "0" }

    graph.forEach { line -> println(line.joinToString("") { it.type }) }

    println("even nodes: ${evenNodes.size}")
}

private fun performDijkstra(graph: Graph, startNode: Node) {
    var unvisited = graph.flatten().toMutableList()

    startNode.distance = 0

    while (unvisited.isNotEmpty()) {
        unvisited = unvisited.sortedBy { it.distance }.toMutableList()

        val currentNode = unvisited.first()
        unvisited.remove(currentNode)

        if (currentNode.distance == Int.MAX_VALUE) {
            break
        }

        val unvisitedNeighbours = findNeighbours(currentNode, graph).filter { unvisited.contains(it) }

        val newDistance = currentNode.distance + 1
        unvisitedNeighbours.forEach { neighbour ->
            if (neighbour.distance > newDistance) {
                neighbour.distance = newDistance
                neighbour.previous = currentNode
            }
        }
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

private fun findNeighbours(source: Node, graph: Graph): List<Node> {
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
    return possibleNeighbour.type == "."
}

private fun partB(graph: Graph) {
    // input is 131x131 tiles
    // it takes 65 steps to reach the end of the input
    // input will expand forever, but since the amount of steps needed to reach the edge is odd,
    // parity for each copied input has to be inverted
    // we get [even, odd, even, odd ...]
    // the pattern of steps forms a diamond
    // this means that at the end of the copying when we have reached the required 26501365 steps
    // there are some corners from copied inputs that we should remove from the total
    // and others that we should add

    // eventually, we come to the following equation
    // (see https://github.com/villuna/aoc23/wiki/A-Geometric-solution-to-advent-of-code-2023,-day-21)

    // total of different squares is
    // (n+1)^2 odd-parity inputs
    // plus
    // n^2 even-parity inputs
    // minus
    // (n-1) odd-parity corners
    // plus
    // n even-parity corners

    // the numbers become pretty big (too big for long)
    // so use BigDecimal

    val smallN = (26501365 - (graph.size / 2)) / graph.size
    val n = BigDecimal.valueOf(smallN.toLong())
    val nPlusOne = n.plus(BigDecimal.ONE)

    val evenTiles = graph.flatten().filter { it.distance % 2 == 0 }
    val oddTiles = graph.flatten().filter { it.distance % 2 == 1 && it.distance != Int.MAX_VALUE }
    // Int.MAX_VALUE is odd, but we do not want to count walls or unreachable tiles

    val evenParityFull = BigDecimal(evenTiles.count())
    val oddParityFull = BigDecimal(oddTiles.count())
    val evenParityCorners = BigDecimal(evenTiles.count { it.distance > 65 })
    val oddParityCorners = BigDecimal(oddTiles.count { it.distance > 65 })


    val totalOddTiles = nPlusOne.times(nPlusOne).times(oddParityFull)
    val totalEvenTiles = n.times(n).times(evenParityFull)
    val totalOddCornerTiles = nPlusOne.times(oddParityCorners)
    val totalEvenCornerTiles = n.times(evenParityCorners)
    val total = totalOddTiles + totalEvenTiles - totalOddCornerTiles + totalEvenCornerTiles

//    println("calculated n to be: $n")
//    println("total even-parity tiles: $evenParityFull")
//    println("total odd-parity tiles: $oddParityFull")
//    println("total even-parity corner tiles: $evenParityCorners")
//    println("total odd-parity corner tiles: $oddParityCorners")
//    println("calculated total odd tiles = $totalOddTiles")
//    println("calculated total even tiles = $totalEvenTiles")
//    println("calculated total odd corner tiles = $totalOddCornerTiles")
//    println("calculated total even corner tiles = $totalEvenCornerTiles")

    println("calculated total tiles = $total")
}




