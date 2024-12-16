package syh.year2024.day16

import syh.AbstractAocDay
import syh.year2023.day23.Puzzle23.Node


class Puzzle16 : AbstractAocDay(2024, 16) {
    override fun doA(file: String): String {
        val lines = readSingleLineFile(file).map { it.split("").filter { c -> c.isNotEmpty() } }
        val grid = mutableListOf<MutableList<Node>>()
        for (j in lines.indices) {
            val row = mutableListOf<Node>()
            for (i in lines[j].indices) {
                row.add(Node(i, j, lines[j][i]))
            }
            grid.add(row)
        }


        val start = grid.flatten().first { it.str == "S" }
        val end = grid.flatten().first { it.str == "E" }

        val junctionGraph = makeJunctionGraph(grid, ::findNeighbours)

//        val maxPath = dfsOptimized(junctionGraph, start, end, mutableMapOf())!!

        val seen = mutableMapOf(start to 0)
        val order = mutableListOf(start)

        val min = dfs(start, end, junctionGraph, seen, order)

//        val allPaths = dfs(start, end, junctionGraph, seen, order)
//        println("found ${allPaths.size} paths")
//        allPaths.forEach { println(it) }

//        val min = allPaths.minOf { calculatePathWorth(it) }

        println("min: $min")
        return min.toString()
    }

    override fun doB(file: String): String {
        return ""
    }

    private fun dfs(current: Node, end: Node, graph: Map<Node, Map<Node, Int>>, seen: MutableMap<Node, Int>, order: MutableList<Node>): Int {
        if (current == end) {
            println("found end")
            println(seen)
            val value = calculatePathWorth(seen, order)
            println("calculated value $value")
            return value
        }

        var lowestResult = Int.MAX_VALUE

        println("checking node ${current.toFullString()}")

        val neighbours = graph[current]!!.filterNot { seen.keys.contains(it.key) }
        for (neighbour in neighbours) {
            seen[neighbour.key] = neighbour.value
            order.add(neighbour.key)

            val dfs = dfs(neighbour.key, end, graph, seen, order)
            if (dfs < lowestResult) {
                lowestResult = dfs
            }

            seen.remove(neighbour.key)
            order.remove(neighbour.key)
        }

        return lowestResult
    }

    private fun findNeighbours(node: Node, grid: MutableList<MutableList<Node>>): List<Node> {
        val neighbours = mutableListOf<Node>()

        if (node.x + 1 in grid[0].indices && grid[node.y][node.x + 1].str != "#") {
            neighbours.add(grid[node.y][node.x + 1])
        }
        if (node.x - 1 in grid[0].indices && grid[node.y][node.x - 1].str != "#") {
            neighbours.add(grid[node.y][node.x - 1])
        }
        if (node.y + 1 in grid.indices && grid[node.y + 1][node.x].str != "#") {
            neighbours.add(grid[node.y + 1][node.x])
        }
        if (node.y - 1 in grid.indices && grid[node.y - 1][node.x].str != "#") {
            neighbours.add(grid[node.y - 1][node.x])
        }

        return neighbours
    }

    private fun calculatePathWorth(path: List<Node>): Int {
        var totalSteps = 1
        var totalDirectionChanges = 1
        var previousDir = findDirection(path[1], path[0])

        for (i in 2..<path.size) {
            totalSteps += 1
            val newDir = findDirection(path[i], path[i - 1])
            if (newDir != previousDir) {
                previousDir = newDir
                totalDirectionChanges += 1
            }
        }

        val total = totalSteps + totalDirectionChanges * 1000
        println("path had $totalSteps steps and $totalDirectionChanges direction changes for a total of $total")

        return total
    }

    private fun calculatePathWorth(path: MutableMap<Node, Int>, order: List<Node>): Int {
        var totalSteps = 0
        var totalDirectionChanges = 1


        var previousDir = findDirection(order[1], order[0])
        for (i in 1..<order.size) {
            totalSteps += path[order[i]]!!

            totalSteps += 1
            val newDir = findDirection(order[i], order[i - 1])
            if (newDir != previousDir) {
                previousDir = newDir
                totalDirectionChanges += 1
            }
        }

        val total = totalSteps + totalDirectionChanges * 1000
        println("path had $totalSteps steps and $totalDirectionChanges direction changes for a total of $total")

        return total
    }

    private fun findDirection(new: Node, old: Node): Direction {
        if (new.x > old.x) return Direction.RIGHT
        if (new.x < old.x) return Direction.LEFT
        if (new.y > old.y) return Direction.UP
        if (new.y < old.y) return Direction.DOWN
        throw IllegalStateException("no difference in nodes $new and $old")
    }


    private fun makeJunctionGraph(graph: MutableList<MutableList<Node>>, findNeighbours: (Node, MutableList<MutableList<Node>>) -> List<Node>): MutableMap<Node, MutableMap<Node, Int>> {
        val adjacencies = graph.indices.flatMap { y ->
            graph[0].indices.mapNotNull { x ->
                if (graph[y][x].str != "#") {
                    val neighbours = findNeighbours(graph[y][x], graph)
                    graph[x][y] to neighbours.associateWith { 1 }.toMutableMap()
                } else null
            }
        }.toMap(mutableMapOf())

        adjacencies.keys.toList().forEach { key ->
            if (key.str == "S" || key.str == "E") {
                // skip
            } else {
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
        }

        println("adjacencies:")
        adjacencies.forEach { println(it) }

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

    enum class Direction { UP, RIGHT, DOWN, LEFT }

    data class Node(
        val x: Int, val y: Int, val str: String, var previous: Node? = null, var distance: Int = Int.MAX_VALUE
    ) {
        override fun toString(): String {
            val previousStr =
                if (previous != null) "with previous [${previous!!.x}][${previous!!.y}]" else "without previous"
            return "[$x][$y]"
        }

        fun toFullString(): String {
            return "[$x][$y]($str)"
        }
    }
}