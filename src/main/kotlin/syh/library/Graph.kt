package syh.library

import java.io.File
import java.io.StringWriter
import java.util.PriorityQueue
import java.util.function.Predicate
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.DirectedWeightedMultigraph
import org.jgrapht.nio.Attribute
import org.jgrapht.nio.DefaultAttribute
import org.jgrapht.nio.dot.DOTExporter


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

    fun removeEdge(from: T, to: T) {
        if (from !in nodes.keys || to !in nodes.keys) {
            println("unknown nodes, edge cannot be removed: $from -> $to")
            return
        }
        val toNode = nodes[to]!!
        nodes[from]!!.neighbours.remove(toNode)
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

    fun writePlantUml(fileName: String) {
        val sb = StringBuilder()
        sb.append("@startuml\n")
        val graphAsString = createGraphvizString()

        sb.append(graphAsString)

        sb.append("@enduml")
        File("${fileName}_graph.puml").writeText(sb.toString())
    }

    private fun createGraphvizString(): String {
        val graph = DirectedWeightedMultigraph<T, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)
        for (vertex in nodes.keys) {
            graph.addVertex(vertex)
        }
        for (node in nodes.values) {
            for ((neighbour, cost) in node.neighbours) {
                graph.setEdgeWeight(graph.addEdge(node.value, neighbour.value), cost.toDouble())
            }
        }

        val exporter = DOTExporter<T, DefaultWeightedEdge>()
        exporter.setVertexAttributeProvider { v ->
            val map: MutableMap<String, Attribute> = LinkedHashMap<String, Attribute>()
            map["label"] = DefaultAttribute.createAttribute(v.toString())
            map
        }

        val writer = StringWriter()
        exporter.exportGraph(graph, writer)
        val graphAsString = writer.toString()
        return graphAsString
    }
}

