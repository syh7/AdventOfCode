package syh.year2022

import syh.library.AbstractAocDay


class Puzzle9 : AbstractAocDay(2022, 9) {

    private val UP = "U"
    private val DOWN = "D"
    private val LEFT = "L"
    private val RIGHT = "R"

    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)

        val snakeSize = 2

        var snake = List(snakeSize) { Position(0, 0) }

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
        return tailPositions.size.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)

        val snakeSize = 10

        var snake = List(snakeSize) { Position(0, 0) }

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
        return tailPositions.size.toString()
    }

    private fun movePosition(snake: List<Position>, direction: String): List<Position> {
        val newSnake = mutableListOf(movePosition(snake[0], direction))
        var lead = newSnake[0]
        for (i in 1..<snake.size) {
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

    data class Position(val x: Int, val y: Int)
}