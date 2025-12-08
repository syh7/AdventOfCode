package syh.year2025

import kotlin.math.pow
import kotlin.math.sqrt
import syh.library.AbstractAocDay
import syh.library.DisjointSet

class Puzzle8 : AbstractAocDay(2025, 8) {

    data class Coord3D(val x: Long, val y: Long, val z: Long) {

        override fun toString(): String {
            return "[$x,$y,$z]"
        }

        fun distance(other: Coord3D): Double {
            return sqrt((other.x - this.x).toDouble().pow(2.0) + (other.y - this.y).toDouble().pow(2.0) + (other.z - this.z).toDouble().pow(2.0))
        }

        companion object {
            fun parse(str: String): Coord3D {
                val (x, y, z) = str.split(",").map { it.toLong() }
                return Coord3D(x, y, z)
            }
        }
    }

    private data class Distance(val i: Int, val j: Int) {

        fun calculate(junctionBoxes: List<Coord3D>): Double {
            val a = junctionBoxes[i]
            val b = junctionBoxes[j]
            return a.distance(b)
        }
    }

    override fun doA(file: String): String {
        val requiredConnections = if (file == "test") 10 else 1000

        val (boxes, distances) = readSingleLineFile(file)
            .map { Coord3D.parse(it) }
            .let { junctionBoxes ->
                val distances = junctionBoxes
                    .flatMapIndexed { i, a ->
                        junctionBoxes.drop(i).mapIndexedNotNull { j, b ->
                            if (a != b) Distance(i, j + i) else null
                        }
                    }
                    .sortedBy { it.calculate(junctionBoxes) }

                junctionBoxes to distances
            }

        val disjointSet = DisjointSet(boxes.size)
        val circuits = Array(boxes.size) { IntArray(boxes.size) }
        var connections = 0
        var distanceIndex = 0

        while (connections < requiredConnections) {
            val distance = distances[distanceIndex++]
            val i = distance.i
            val j = distance.j

            if (circuits[i][j] == 0) {
                circuits[i][j] = 1
                circuits[j][i] = 1
                disjointSet.union(i, j)
                connections++
            }
        }

        return disjointSet.getAllComponentSizes()
            .sortedByDescending { it }
            .distinct()
            .take(3)
            .fold(1L) { acc, size -> acc * size }
            .toString()
    }

    override fun doB(file: String): String {
        val (boxes, distances) = readSingleLineFile(file)
            .map { Coord3D.parse(it) }
            .let { junctionBoxes ->
                val distances = junctionBoxes
                    .flatMapIndexed { i, a ->
                        junctionBoxes.mapIndexedNotNull { j, b ->
                            if (a != b) Distance(i, j) else null
                        }
                    }
                    .sortedBy { it.calculate(junctionBoxes) }

                junctionBoxes to distances
            }

        val disjointSet = DisjointSet(boxes.size)
        var distanceIndex = 0

        while (true) {
            val distance = distances[distanceIndex++]
            val i = distance.i
            val j = distance.j

            disjointSet.union(i, j)

            if (disjointSet.getAllComponentSizes().size == 1) {
                return (boxes[i].x * boxes[j].x).toString()
            }
        }
    }
}