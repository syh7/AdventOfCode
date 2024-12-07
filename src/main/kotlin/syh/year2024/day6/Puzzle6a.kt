package syh.year2024.day6

import syh.readSingleLineFile


fun main() {

    val badCoordInit = Coord(-1, -1)
    var start: Coord = badCoordInit
    val graph = readSingleLineFile("year2024/day6/actual.txt")
        .map { line -> line.split("").filter { it.isNotEmpty() } }
        .mapIndexed { y, strings ->
            strings.mapIndexed { x, c ->
                if (c == "^") {
                    start = Coord(x, y)
                    Location.FREE
                } else {
                    Location.values().first { it.mapValue == c }
                }
            }
        }

    if (start == badCoordInit) {
        throw IllegalStateException("could not find start")
    }

    val path = calculatePath(start, graph)
    println("total length for distinct path A = ${path.distinct().size}")

    println()
    println()
    println()
    println()

    var totalObstacles = 0
    for (node in path.distinct()) {
//        println("trying coord $node")
        if (simulateObstacle(node.x, node.y, start, graph)) {
//            println("loop at ${node.toCoordString()}")
            totalObstacles++
        }
    }
    println("total obstacles: $totalObstacles")
}

private fun calculatePath(start: Coord, graph: List<List<Location>>): MutableList<Coord> {
    val path = mutableListOf(start)
    var current = start
    var direction = Direction.UP

    val ySize = graph.size
    val xSize = graph[0].size

    while (true) {
        val tryDirection = move(direction, current)

        if (graph[tryDirection.y][tryDirection.x] == Location.FREE) {
            path.add(tryDirection)
            current = tryDirection
            println("went straight at ${current.toCoordString()}")

            if (reachedEnd(current.x, xSize) || reachedEnd(current.y, ySize)) {
                println("found end at ${current.toCoordString()}")
                break
            }

        } else {
            direction = turnRight(direction)
            println("turned right at ${current.toCoordString()}")
        }

    }

    return path
}

private fun simulateObstacle(
    obstacleX: Int,
    obstacleY: Int,
    start: Coord,
    graph: List<List<Location>>
): Boolean {
    val path = mutableListOf<Pair<Coord, Direction>>()
    var current = start
    var direction = Direction.UP

    val ySize = graph.size
    val xSize = graph[0].size

    while (true) {
        path.add(current to direction)

        val tryDirection = move(direction, current)

        if (tryDirection.x == obstacleX && tryDirection.y == obstacleY) {
            direction = turnRight(direction)
        } else {

            if (graph[tryDirection.y][tryDirection.x] == Location.FREE) {
                if (path.contains(tryDirection to direction)) {
//                println("found loop")
//                println(path)
                    return true
                }
                path.add(tryDirection to direction)
                current = tryDirection

                if (reachedEnd(current.x, xSize) || reachedEnd(current.y, ySize)) {
//                    println("found end at ${current.toCoordString()}")
                    return false
                }

            } else {
                direction = turnRight(direction)
            }
        }

    }

}

private fun reachedEnd(current: Int, size: Int) = current == 0 || current == size - 1

private fun move(direction: Direction, current: Coord) = when (direction) {
    Direction.UP -> Coord(current.x, current.y - 1)
    Direction.RIGHT -> Coord(current.x + 1, current.y)
    Direction.DOWN -> Coord(current.x, current.y + 1)
    Direction.LEFT -> Coord(current.x - 1, current.y)
}

private fun turnRight(oldDirection: Direction): Direction {
    val oldIndex = Direction.values().indexOf(oldDirection)
    val newIndex = (oldIndex + 1) % Direction.values().size
    return Direction.values()[newIndex]
}

enum class Direction { UP, RIGHT, DOWN, LEFT }

enum class Location(val mapValue: String) { FREE("."), OBSTRUCTION("#") }

data class Coord(val x: Int, val y: Int) {
    fun toCoordString(): String {
        return "[$x][$y]"
    }
}
