package syh.year2023

import org.jgrapht.alg.StoerWagnerMinimumCut
import org.jgrapht.graph.DefaultWeightedEdge
import org.jgrapht.graph.SimpleWeightedGraph
import syh.library.AbstractAocDay

class Puzzle25 : AbstractAocDay(2023, 25) {
    override fun doA(file: String): String {

        val graph = SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge::class.java)

        readSingleLineFile(file)
            .map {
                val (start, otherStr) = it.split(": ")
                val others = otherStr.split(" ")

                graph.addVertex(start)
                for (other in others) {
                    println("adding edges between $start and $other")
                    graph.addVertex(other)
                    graph.addEdge(start, other)
                }
            }

        val oneComponent = StoerWagnerMinimumCut(graph).minCut()
        val otherComponent = graph.vertexSet() - oneComponent

        println(oneComponent)
        println(otherComponent)

        return (oneComponent.size * otherComponent.size).toString()
    }

    override fun doB(file: String): String {
        // there is no part B
        return ""
    }
}