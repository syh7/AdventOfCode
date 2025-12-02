package syh.year2024

import syh.library.AbstractAocDay

class Puzzle6 : AbstractAocDay(2024, 6) {
    override fun doA(file: String): String {
        val (start, graph) = readStartAndGraph(file)
        val path = calculatePath(start, graph)
        println("total length for distinct path A = ${path.distinct().size}")
        return path.distinct().size.toString()
    }

    override fun doB(file: String): String {
        val (start, graph) = readStartAndGraph(file)
        val path = calculatePath(start, graph)
        var totalObstacles = 0L
        for ((counter, node) in path.distinct().withIndex()) {
            println("trying node $counter: ${node.toCoordString()}")
            if (simulateObstacle(node.x, node.y, start, graph)) {
                totalObstacles++
            }
        }
        println("total obstacles: $totalObstacles")
        return totalObstacles.toString()
    }

    private fun readStartAndGraph(file: String): Pair<Coord, List<List<Location>>> {
        val badCoordInit = Coord(-1, -1)
        var start: Coord = badCoordInit
        val graph = readSingleLineFile(file)
            .map { line -> line.split("").filter { it.isNotEmpty() } }
            .mapIndexed { y, strings ->
                strings.mapIndexed { x, c ->
                    if (c == "^") {
                        start = Coord(x, y)
                        Location.FREE
                    } else {
                        Location.entries.first { it.mapValue == c }
                    }
                }
            }

        if (start == badCoordInit) {
            throw IllegalStateException("could not find start")
        }
        return start to graph
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
//                println("went straight at ${current.toCoordString()}")

                if (reachedEnd(current.x, xSize) || reachedEnd(current.y, ySize)) {
//                    println("found end at ${current.toCoordString()}")
                    break
                }

            } else {
                direction = turnRight(direction)
//                println("turned right at ${current.toCoordString()}")
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
        val path = mutableSetOf<Pair<Coord, Direction>>()
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
        val oldIndex = Direction.entries.indexOf(oldDirection)
        val newIndex = (oldIndex + 1) % Direction.entries.size
        return Direction.entries[newIndex]
    }

    enum class Direction { UP, RIGHT, DOWN, LEFT }

    enum class Location(val mapValue: String) { FREE("."), OBSTRUCTION("#") }

    data class Coord(val x: Int, val y: Int) {
        fun toCoordString(): String {
            return "[$x][$y]"
        }
    }
}