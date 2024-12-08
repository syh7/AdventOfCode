package syh.year2024.day8

import syh.AbstractAocDay


class Puzzle8 : AbstractAocDay(2024, 8) {
    override fun doA(file: String): Long {
        val graph = readGraph(file)

        val antennaGroups = graph.flatten().filter { it.antenna }.groupBy { it.frequency }

        for (antennaGroup in antennaGroups.values) {
            println("checking antenna group ${antennaGroup[0].frequency} with ${antennaGroup.size} members")
            for (i in antennaGroup.indices) {
                for (j in antennaGroup.indices) {
                    if (i == j) continue
                    val nodeA = antennaGroup[i]
                    val nodeB = antennaGroup[j]
                    val deltaX = nodeA.x - nodeB.x
                    val deltaY = nodeA.y - nodeB.y

                    println("checking [${nodeA.y}][${nodeA.x}] with [${nodeB.y}][${nodeB.x}] with difference [$deltaY][$deltaX]")

                    if (nodeA.x + deltaX in graph[0].indices && nodeA.y + deltaY in graph.indices) {
                        println("setting antinode on [${nodeA.y + deltaY}][${nodeA.x + deltaX}]")
                        graph[nodeA.y + deltaY][nodeA.x + deltaX].antiNode = true
                    }
                    if (nodeB.x - deltaX in graph[0].indices && nodeB.y - deltaY in graph.indices) {
                        println("setting antinode on [${nodeB.y - deltaY}][${nodeB.x - deltaX}]")
                        graph[nodeB.y - deltaY][nodeB.x - deltaX].antiNode = true
                    }
                }
            }
        }

        for (i in graph.indices) {
            println("$i: ${graph[i].map { if (it.antiNode) '#' else it.frequency }}")
        }

        return graph.flatten().count { it.antiNode }.toLong()
    }

    override fun doB(file: String): Long {
        val graph = readGraph(file)

        val antennaGroups = graph.flatten().filter { it.antenna }.groupBy { it.frequency }

        for (antennaGroup in antennaGroups.values) {
            println("checking antenna group ${antennaGroup[0].frequency} with ${antennaGroup.size} members")
            for (i in antennaGroup.indices) {
                for (j in antennaGroup.indices) {
                    if (i == j) continue
                    val nodeA = antennaGroup[i]
                    val nodeB = antennaGroup[j]
                    val deltaX = nodeA.x - nodeB.x
                    val deltaY = nodeA.y - nodeB.y

                    println("checking [${nodeA.y}][${nodeA.x}] with [${nodeB.y}][${nodeB.x}] with difference [$deltaY][$deltaX]")

                    // switch the starting node for each delta so that the starting nodes are also antinodes
                    setAntinode(nodeA.x, nodeA.y, -deltaX, -deltaY, graph)
                    setAntinode(nodeB.x, nodeB.y, deltaX, deltaY, graph)
                }
            }
        }

        for (i in graph.indices) {
            println("$i: ${graph[i].map { if (it.antiNode) '#' else it.frequency }.joinToString("")}")
        }

        return graph.flatten().count { it.antiNode }.toLong()
    }

    private fun setAntinode(startX: Int, startY: Int, deltaX: Int, deltaY: Int, graph: MutableList<MutableList<Node>>) {
        println("setting antinodes in line with [$startY][$startX] and delta [$deltaY][$deltaX]")
        for (i in 1..<graph.size) {
            if (startX + deltaX * i in graph[0].indices && startY + deltaY * i in graph.indices) {
                println("setting antinode on [${startY + deltaY * i}][${startX + deltaX * i}]")
                graph[startY + deltaY * i][startX + deltaX * i].antiNode = true
            } else {
                break
            }
        }
    }

    private fun readGraph(file: String): MutableList<MutableList<Node>> {
        val chars = readSingleLineFile(file)
            .map { it.split("").filter { c -> c.isNotEmpty() } }

        val graph = mutableListOf<MutableList<Node>>()
        for (y in chars.indices) {
            val row = mutableListOf<Node>()
            for (x in chars[0].indices) {
                row.add(Node(x, y, chars[y][x] != ".", chars[y][x]))
            }
            graph.add(row)
        }

        for (i in graph.indices) {
            println("$i: ${graph[i].map { it.frequency }}")
        }
        return graph
    }


    data class Node(val x: Int, val y: Int, val antenna: Boolean, val frequency: String, var antiNode: Boolean = false)
}