package syh.library

import java.util.*
import java.util.function.Predicate


/**
 * Graph of type T has nodes that contain the same type T
 */
data class Graph<T>(val nodes: MutableMap<T, Node<T>> = mutableMapOf()) {

    fun findNodesSatisfying(predicate: Predicate<T>): List<T> {
        return nodes.keys.filter { predicate.test(it) }
    }

    fun getNode(t: T): Node<T>? {
        return nodes[t]
    }

    fun addNode(t: T) {
        if (!nodes.containsKey(t)) {
            nodes[t] = Node(t)
        }
    }

    fun addEdge(from: T, to: T, cost: Long) {
        addNode(from)
        addNode(to)
        val fromNode = nodes[from]!!
        val toNode = nodes[to]!!
        fromNode.neighbours[toNode] = cost
    }

    fun reset() {
        this.nodes.values.forEach { node -> node.reset() }
    }

    fun dijkstra(from: T): Set<Node<T>> {
        val visited = mutableSetOf<Node<T>>()
        val queue = PriorityQueue<Node<T>>(nodes.size)

        // Set start point
        nodes[from]!!.distance = 0
        queue.add(nodes[from])

        while (!queue.isEmpty()) {
            val node = queue.poll()

            // skip checked nodes
            if (node in visited) {
                continue
            }

            // Iterate over all neighbors
            for (neighbour in node.neighbours.keys) {

                // if neighbour is not seen
                if (!visited.contains(neighbour)) {

                    // reset predecessors and distance if new distance is better
                    if (neighbour.distance > node.distance + node.neighbours[neighbour]!!) {
                        // This path is shorter, clear predecessors
                        neighbour.predecessors.clear()
                        neighbour.distance = node.distance + node.neighbours[neighbour]!!
                    }

                    // if distance is better or equal, add predecessor
                    if (neighbour.distance == node.distance + node.neighbours[neighbour]!!) {
                        neighbour.predecessors.add(node)
                    }

                    // add neighbour to queue to visit next
                    queue.add(neighbour)
                }
            }

            visited.add(node)
        }

        return visited
    }
}

data class Node<V>(
    val value: V,
    val neighbours: MutableMap<Node<V>, Long> = mutableMapOf(),
    val predecessors: MutableList<Node<V>> = mutableListOf(),
    var distance: Long = Long.MAX_VALUE
) : Comparable<Node<V>> {

    fun reset() {
        this.predecessors.clear()
        this.distance = Long.MAX_VALUE
    }

    override fun compareTo(other: Node<V>): Int {
        return this.distance.compareTo(other.distance)
    }

    override fun toString(): String {
        return "Node($value, $distance)"
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other.javaClass != this.javaClass) return false
        return this.value?.equals((other as Node<V>).value) ?: false
    }

}