package syh.year2024

import syh.library.AbstractAocDay
import syh.library.Coord
import syh.library.Direction
import syh.library.Graph
import syh.library.Node

class Puzzle18 : AbstractAocDay(2024, 18) {
    override fun doA(file: String): String {
        val size = if (file == "test") 6 else 70
        println("file $file means size $size")
        val wallsToTake = if (file == "test") 12 else 1024
        println("file $file means walls to take $wallsToTake")

        val wallCoords = readSingleLineFile(file)
            .take(wallsToTake)
            .map {
                val (x, y) = it.split(",").filter { c -> c.isNotEmpty() }
                Coord(row = y.toInt(), column = x.toInt())
            }

        val graph = readGraph(size, wallCoords)

        val startCoord = Coord(0, 0)
        val endCoord = Coord(size, size)

        val visited = graph.dijkstra(startCoord)
        for (visit in visited) {
            println("visited $visit")
        }

        val endNode = graph.getNode(endCoord)!!
        println("endnode $endNode")
        return endNode.distance.toString()

    }

    override fun doB(file: String): String {

        val size = if (file == "test") 6 else 70
        println("file $file means size $size")
        var wallsToTake = if (file == "test") 12 else 1024
        println("file $file means walls to take $wallsToTake")

        val wallCoords = readSingleLineFile(file)
            .map {
                val (x, y) = it.split(",").filter { c -> c.isNotEmpty() }
                Coord(row = y.toInt(), column = x.toInt())
            }

        val startCoord = Coord(0, 0)
        val endCoord = Coord(size, size)


        while (true) {
            val (graph, _) = createGraph(size, wallCoords, wallsToTake, startCoord)
            val endNode = graph.getNode(endCoord)!!
//            println("endnode $endNode")

            if (endNode.distance == Long.MAX_VALUE) {
                println("found max distance end node that could not be reached")
                break
            }

            val endPredecessors = endNode.getAllPredecessors().map { it.value }
            val obstructingWallIndex = wallCoords.drop(wallsToTake).indexOfFirst { it in endPredecessors }
            wallsToTake += obstructingWallIndex + 1 // add 1 because the new index is 1 off because it starts from 0 after dropping
            println("new wall number: $wallsToTake")
        }
        wallsToTake -= 1 // remove 1 because we added 1 earlier
        val blockingWall = wallCoords[wallsToTake]
        println("final wall is $blockingWall at $wallsToTake")

        return "${blockingWall.column},${blockingWall.row}"
    }

    private fun createGraph(size: Int, wallCoords: List<Coord>, wallsToTake: Int, startCoord: Coord): Pair<Graph<Coord>, Set<Node<Coord>>> {
        val graph = readGraph(size, wallCoords.take(wallsToTake))
        val visited = graph.dijkstra(startCoord)
        return graph to visited
    }

    private fun readGraph(size: Int, wallCoords: List<Coord>): Graph<Coord> {
        val graph = Graph<Coord>()

        val sizeRange = 0..size
        for (row in sizeRange) {
            for (column in sizeRange) {
                val coord = Coord(row, column)
//                println("checking coord $coord")
                val neighbours = findNeighbour(coord, wallCoords, sizeRange)
                for (neighbour in neighbours) {
//                    println("coord $coord has neighbour $neighbour")
                    graph.addEdge(coord, neighbour, 1)
                }
            }
        }
//        printGrid(sizeRange, wallCoords)
        return graph
    }

    private fun printGrid(sizeRange: IntRange, wallCoords: List<Coord>) {
        for (row in sizeRange) {
            for (column in sizeRange) {
                val coord = Coord(row, column)
                if (coord in wallCoords) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }

    private fun findNeighbour(coord: Coord, walls: List<Coord>, size: IntRange): MutableList<Coord> {
        val neighbours = mutableListOf<Coord>()

        for (direction in Direction.CARDINAL_DIRECTIONS) {
            val neighbourCoord = coord.relative(direction)
            if (neighbourCoord.row in size && neighbourCoord.column in size) {
                if (walls.contains(neighbourCoord)) {
//                    println("coord $coord meets wall at $neighbourCoord")
                } else {
                    neighbours.add(neighbourCoord)

                }
            }
        }

        return neighbours
    }
}