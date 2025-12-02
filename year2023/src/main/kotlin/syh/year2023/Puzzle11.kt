package syh.year2023

import kotlin.math.abs
import syh.library.AbstractAocDay


class Puzzle11 : AbstractAocDay(2023, 11) {
    private val GALAXY = "#"
    private val SPACE = "."

    override fun doA(file: String): String {
        val lines = readSingleLineFile(file).map { line ->
            line.split("").filter { it.isNotEmpty() }.toMutableList()
        }.toMutableList()

        printGalaxy(lines)
        println()

        val emptyRows = findEmptyRows(lines)
        val emptyColumns = findEmptyColumns(lines)

        val graph = readGraph(lines)
        val spaceMultiplier = 2

        val distanceMap = createDistanceMap(graph, emptyRows, emptyColumns, spaceMultiplier)

        val totalDistance = distanceMap.values.sum()

        println("total distance for A is $totalDistance")
        return totalDistance.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file).map { line ->
            line.split("").filter { it.isNotEmpty() }.toMutableList()
        }.toMutableList()

        printGalaxy(lines)
        println()

        val emptyRows = findEmptyRows(lines)
        val emptyColumns = findEmptyColumns(lines)

        val graph = readGraph(lines)
        val spaceMultiplier = 1000000

        val distanceMap = createDistanceMap(graph, emptyRows, emptyColumns, spaceMultiplier)

        val totalDistance = distanceMap.values.sum()

        println("total distance for B is $totalDistance")
        return totalDistance.toString()
    }

    private fun createDistanceMap(
        graph: MutableList<MutableList<Node>>,
        emptyRows: MutableList<Int>,
        emptyColumns: MutableList<Int>,
        spaceMultiplierForA: Int
    ): MutableMap<Pair<Coord, Coord>, Long> {
        val galaxies = graph.flatten().filter { it.value == GALAXY }

        val distanceMap = mutableMapOf<Pair<Coord, Coord>, Long>()

        for (galaxy in galaxies) {
            for (otherGalaxy in galaxies) {
                if (galaxy == otherGalaxy) {
                    continue
                }
                if (distanceMap.contains(Pair(galaxy.coord, otherGalaxy.coord))) {
                    continue
                }
                val distance = findDistance(galaxy, otherGalaxy)

                val crossedEmptyRows = emptyRows.count { it in createMinMaxRange(galaxy.coord.x, otherGalaxy.coord.x) }
                val crossedEmptyColumns = emptyColumns.count { it in createMinMaxRange(galaxy.coord.y, otherGalaxy.coord.y) }

                val multipliedDistance = distance +
                        (crossedEmptyRows * (spaceMultiplierForA - 1)) +
                        (crossedEmptyColumns * (spaceMultiplierForA - 1))

                distanceMap[Pair(galaxy.coord, otherGalaxy.coord)] = multipliedDistance

                // path between X and Y is same as path between Y and X, do not count the path double
                distanceMap[Pair(otherGalaxy.coord, galaxy.coord)] = 0
            }
        }
        return distanceMap
    }

    private fun createMinMaxRange(a: Int, b: Int): IntRange {
        val list = listOf(a, b)
        return list.min()..list.max()
    }

    private fun findDistance(galaxy: Node, otherGalaxy: Node): Long {
        val deltaX = abs(galaxy.coord.x - otherGalaxy.coord.x)
        val deltaY = abs(galaxy.coord.y - otherGalaxy.coord.y)
        return (deltaX + deltaY).toLong()
    }

    private fun readGraph(input: List<List<String>>): MutableList<MutableList<Node>> {
        val allNodes = mutableListOf<MutableList<Node>>()
        for (i in input.indices) {
            val rowNodes = mutableListOf<Node>()
            for (j in input[i].indices) {
                rowNodes.add(Node(Coord(i, j), value = input[i][j]))
            }
            allNodes.add(rowNodes)
        }
        return allNodes
    }

    private fun findEmptyRows(lines: MutableList<MutableList<String>>): MutableList<Int> {
        val indexes = mutableListOf<Int>()
        for (i in lines.indices) {
            if (lines[i].all { it == SPACE }) {
                indexes.add(i)
            }
        }
        return indexes
    }

    private fun findEmptyColumns(lines: MutableList<MutableList<String>>): MutableList<Int> {
        val indexes = mutableListOf<Int>()
        for (j in lines[0].indices) {
            var allDots = true
            for (i in lines.indices) {
                if (lines[i][j] != SPACE) {
                    allDots = false
                }
            }
            if (allDots) {
                indexes.add(j)
            }
        }
        return indexes
    }

    private fun printGalaxy(lines: List<List<String>>) {
        lines.forEach { line -> println(line.joinToString("") { it }) }
    }

    data class Node(val coord: Coord, val value: String, var previous: Node? = null, var distance: Long = Long.MAX_VALUE) {
        override fun toString(): String {
            val previousStr =
                if (previous != null) "with previous ${previous!!.coord}" else "without previous"
            return "$coord, distance $distance, $previousStr"
        }
    }

    data class Coord(val x: Int, val y: Int) {
        override fun toString(): String {
            return "[$x][$y]"
        }
    }

}