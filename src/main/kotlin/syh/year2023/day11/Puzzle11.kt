package syh.year2023.day11

import syh.readSingleLineFile
import kotlin.math.abs

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

const val GALAXY = "#"
const val SPACE = "."

typealias Graph = MutableList<MutableList<Node>>

fun main() {
    val lines = readSingleLineFile("year2023/day11/actual.txt").map { line ->
        line.split("").filter { it.isNotEmpty() }.toMutableList()
    }.toMutableList()

    printGalaxy(lines)
    println()

    val spaceMultiplierForA = 2
    val spaceMultiplierForB = 1000000

    val emptyRows = findEmptyRows(lines)
    val emptyColumns = findEmptyColumns(lines)

    printGalaxy(lines)

    val graph = readGraph(lines)
    val galaxies = graph.flatten().filter { it.value == GALAXY }


    val distanceMapA = mutableMapOf<Pair<Coord, Coord>, Long>()
    val distanceMapB = mutableMapOf<Pair<Coord, Coord>, Long>()

    for (galaxy in galaxies) {
        for (otherGalaxy in galaxies) {
            if (galaxy == otherGalaxy) {
                continue
            }
            if (distanceMapA.contains(Pair(galaxy.coord, otherGalaxy.coord))) {
                continue
            }
            val distance = findDistance(galaxy, otherGalaxy)

            val crossedEmptyRows =
                emptyRows.count { it in createMinMaxRange(galaxy.coord.x, otherGalaxy.coord.x) }
            val crossedEmptyColumns =
                emptyColumns.count { it in createMinMaxRange(galaxy.coord.y, otherGalaxy.coord.y) }

            val distanceA =
                distance + (crossedEmptyRows * (spaceMultiplierForA - 1)) + (crossedEmptyColumns * (spaceMultiplierForA - 1))
            val distanceB =
                distance + (crossedEmptyRows * (spaceMultiplierForB - 1)) + (crossedEmptyColumns * (spaceMultiplierForB - 1))

            distanceMapA[Pair(galaxy.coord, otherGalaxy.coord)] = distanceA
            distanceMapB[Pair(galaxy.coord, otherGalaxy.coord)] = distanceB

            // path between X and Y is same as path between Y and X, do not count the path double
            distanceMapB[Pair(otherGalaxy.coord, galaxy.coord)] = 0
            distanceMapA[Pair(otherGalaxy.coord, galaxy.coord)] = 0
        }
    }

    val totalDistanceA = distanceMapA.values.sum()
    val totalDistanceB = distanceMapB.values.sum()

    println("total distance for A is $totalDistanceA")
    println("total distance for B is $totalDistanceB")

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


private fun readGraph(input: List<List<String>>): Graph {
    val allNodes: Graph = mutableListOf()
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