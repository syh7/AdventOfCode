package syh.library

import java.util.*


/**
 * Graph of type T has nodes that contain the same type T
 */
data class Graph<T>(val nodes: MutableMap<T, Node<T>> = mutableMapOf()) {

    fun addNode(node: T) {
        if (!nodes.containsKey(node)) {
            nodes[node] = Node(node)
        }
    }

    fun addEdge(from: T, to: T, cost: Long) {
        addNode(from)
        addNode(to)
        val fromNode = nodes[from]!!
        val toNode = nodes[to]!!
        fromNode.neighbours[toNode] = cost
    }

    fun dijkstra(from: T): Set<Node<T>> {
        val visited = mutableSetOf<Node<T>>()
        val queue = PriorityQueue<Node<T>>(nodes.size)

        // Set start point
        nodes[from]!!.distance = 0
        queue.add(nodes[from])

        while (!queue.isEmpty()) {
            val node = queue.poll()
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

    fun dijkstra(from: T, to: T): Long {
        for (node in dijkstra(from)) {
            if (nodes[to] == node) {
                return node.distance
            }
        }
        return -1
    }
}

data class Node<V>(
    val value: V,
    val neighbours: MutableMap<Node<V>, Long> = mutableMapOf(),
    val predecessors: MutableList<Node<V>> = mutableListOf(),
    var distance: Long = Long.MAX_VALUE
) : Comparable<Node<V>> {
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
        return this.value?.equals((other as Node<*>).value) ?: false
    }

}