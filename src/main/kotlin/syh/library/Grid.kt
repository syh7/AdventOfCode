package syh.library

import java.util.Stack
import java.util.function.Function

class Grid<T> {

    val grid = mutableListOf<MutableList<Node<Pair<Coord, T>>>>()

    fun locationOf(t: T): Coord {
        for (row in grid.indices) {
            for (column in grid[0].indices) {
                if (grid[row][column].value.second == t) {
                    return Coord(row, column)
                }
            }
        }
        throw IllegalStateException("value $t is not in grid")
    }

    fun fill(input: List<List<String>>, converter: Function<String, T>) {
        grid.clear()
        for (rowIndex in input.indices) {
            val row = mutableListOf<Node<Pair<Coord, T>>>()
            for (columnIndex in input[0].indices) {
                row.add(Node(Coord(rowIndex, columnIndex) to converter.apply(input[rowIndex][columnIndex])))
            }
            grid.add(row)
        }
    }

    fun floodFill(start: Coord, findNeighbours: Function<Coord, List<Coord>>, neighbourTest: Function<T, Boolean>) {
        val visited = mutableSetOf<Node<Pair<Coord, T>>>()

        val startNode = at(start)
        startNode.distance = 0

        val stack = Stack<Node<Pair<Coord, T>>>()
        stack.add(startNode)

        while (stack.isNotEmpty()) {
            val node = stack.pop()
            if (visited.contains(node)) {
                continue
            }

            visited.add(node)
            val newDistance = node.distance + 1

            val neighbours = findNeighbours.apply(node.value.first)
//            println("checking ${node.coord} with distance ${node.distance} and neighbours ${neighbours.map { it.toCoordString() }}")
            for (neighbour in neighbours) {
                val neighbourNode = at(neighbour)
                if (neighbourTest.apply(neighbourNode.value.second)) {
                    if (neighbourNode.distance > newDistance) {
                        neighbourNode.predecessors.clear()
                        neighbourNode.predecessors.add(node)
                        neighbourNode.distance = newDistance
                    }
                    if (newDistance == neighbourNode.distance) {
                        neighbourNode.predecessors.add(node)
                    }
                    stack.add(neighbourNode)
                }
            }
        }
    }

    fun at(coord: Coord): Node<Pair<Coord, T>> {
        return grid[coord.row][coord.column]
    }

}