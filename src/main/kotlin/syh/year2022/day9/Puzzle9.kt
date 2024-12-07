package syh.year2022.day9

import syh.readSingleLineFile

data class Position(val x: Int, val y: Int)

const val UP = "U"
const val DOWN = "D"
const val LEFT = "L"
const val RIGHT = "R"

fun main() {
    val lines = readSingleLineFile("year2022/day9/actual.txt")

    val snakeSizeForA = 2
    val snakeSizeForB = 10

    var snake = List(snakeSizeForB) { Position(0, 0) }

    val tailPositions = mutableSetOf<Position>()
    tailPositions.add(snake.last())

    for (line in lines) {
        val (direction, amount) = line.split(" ")

        for (i in 1..amount.toInt()) {
            snake = movePosition(snake, direction)
            tailPositions.add(snake.last())
        }
    }

    println(tailPositions.size)

}

private fun movePosition(snake: List<Position>, direction: String): List<Position> {
    val newSnake = mutableListOf(movePosition(snake[0], direction))
    var lead = newSnake[0]
    for (i in 1 until snake.size) {
        val newFollow = moveFollowPosition(lead, snake[i])
        newSnake.add(newFollow)
        lead = newFollow
    }
    return newSnake
}

private fun movePosition(position: Position, direction: String): Position {
    return when (direction) {
        UP -> Position(position.x + 1, position.y)
        DOWN -> Position(position.x - 1, position.y)
        RIGHT -> Position(position.x, position.y + 1)
        LEFT -> Position(position.x, position.y - 1)
        else -> throw IllegalStateException()
    }
}

private fun moveFollowPosition(lead: Position, follow: Position): Position {
    if (follow.x in lead.x - 1..lead.x + 1 && follow.y in lead.y - 1..lead.y + 1) {
        return follow
    }

    val deltaX = lead.x.compareTo(follow.x)
    val deltaY = lead.y.compareTo(follow.y)

    return Position(follow.x + deltaX, follow.y + deltaY)
}
