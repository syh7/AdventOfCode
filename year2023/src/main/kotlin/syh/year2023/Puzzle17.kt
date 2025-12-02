package syh.year2023

import syh.library.readSingleLineFile


enum class Direction(val stringRepresentation: String) { NORTH("^"), EAST(">"), SOUTH("v"), WEST("<") }

data class DirectionLength(var direction: Direction?, var length: Int) {
    fun allowsDirection(newDirection: Direction, maxDirectionLength: Int): Boolean {
        // allow new direction
        if (direction != newDirection) {
            return true
        }
        // allow maximum 3 in the same direction
        return length <= maxDirectionLength
    }

    fun addDirection(newDirection: Direction): DirectionLength {
        if (newDirection == direction) {
            return DirectionLength(newDirection, length + 1)
        }
        return DirectionLength(newDirection, 1)
    }

    fun isTurnDirection(newDirection: Direction): Boolean {
        return when (direction) {
            Direction.NORTH, Direction.SOUTH -> {
                newDirection == Direction.EAST || newDirection == Direction.WEST
            }

            Direction.EAST, Direction.WEST -> {
                newDirection == Direction.NORTH || newDirection == Direction.SOUTH
            }

            else -> { // start will have no direction, then all new directions are turns
                true
            }
        }
    }
}

data class CityBlock(
    val heatLoss: Int,
    val coord: Coord,
    var totalHeatLoss: Int = Int.MAX_VALUE,
    var previous: CityBlock? = null,
    var previousDirection: Direction? = null,
    var directionLength: Int = 0,
) {

    override fun toString(): String {
        return "${toCoordString()} with total heatloss $totalHeatLoss has previous ${previous?.toCoordString()}"
    }

    fun toCoordString(): String {
        return coord.toCoordString()
    }
}

data class Coord(val x: Int, val y: Int) {
    fun toCoordString(): String {
        return "[$x][$y]"
    }
}

typealias Grid = MutableList<MutableList<CityBlock>>

private fun Grid.prettyPrint() {
    this.forEach { line -> println(line.joinToString("") { it.heatLoss.toString() }) }
}

fun main() {
    val lines = readSingleLineFile("year2023/day17/test.txt")

    val grid: Grid = mutableListOf()

    for (i in lines.indices) {
        val row = mutableListOf<CityBlock>()
        val split = lines[i].split("").filter { it.isNotBlank() }
        for (j in split.indices) {
            row.add(CityBlock(heatLoss = split[j].toInt(), coord = Coord(i, j)))
        }
        grid.add(row)
    }

    findPath(grid)

    println(grid.last().last())

}

private fun findPath(grid: Grid) {

    val visited = mutableListOf<CityBlock>()

    val startBlock = grid[0][0]
    startBlock.totalHeatLoss = 0

    val blocksToTraverse = mutableListOf(startBlock)

    while (blocksToTraverse.isNotEmpty()) {
        blocksToTraverse.sortBy { it.totalHeatLoss }

        val currentBlock = blocksToTraverse.first()
        blocksToTraverse.remove(currentBlock)

        println("Currently handling ${currentBlock.toCoordString()}")

        if (visited.contains(currentBlock)) {
            println("Already visited so skipping")
            continue
        }

        val currentDirectionLength = DirectionLength(currentBlock.previousDirection, currentBlock.directionLength)

        val neighbourPairs = findNeighbours(currentBlock, currentDirectionLength, grid)
            .filterNot { visited.contains(it.first) }
        for (neighbour in neighbourPairs) {
            val totalHeat = currentBlock.totalHeatLoss + neighbour.first.heatLoss
            if (totalHeat < neighbour.first.totalHeatLoss) {
                println("New total heat for ${neighbour.first.toCoordString()}: $totalHeat")
                neighbour.first.totalHeatLoss = totalHeat
                neighbour.first.previous = currentBlock
                neighbour.first.previousDirection = neighbour.second.direction
                neighbour.first.directionLength = neighbour.second.length
            }
        }

        blocksToTraverse.addAll(neighbourPairs.map { it.first })
        visited.add(currentBlock)

        println()
    }

    var pathBlock: CityBlock? = grid.last().last()
    val path = mutableListOf<CityBlock>()
    while (pathBlock != null) {
        println("Path block ${pathBlock.toCoordString()} has total heat ${pathBlock.totalHeatLoss} and direction ${pathBlock.previousDirection} with length ${pathBlock.directionLength}")
        path.add(pathBlock)
        pathBlock = pathBlock.previous
    }

    printGridAndPath(grid, path)
}

private fun printGridAndPath(grid: Grid, path: List<CityBlock>) {
    for (row in grid) {
        for (block in row) {
            if (path.contains(block)) {
                print(block.previousDirection?.stringRepresentation ?: block.heatLoss)
            } else {
                print(block.heatLoss)
            }
        }
        println()
    }
}

private fun findNeighbours(
    source: CityBlock,
    currentDirectionLength: DirectionLength,
    graph: Grid
): MutableList<Pair<CityBlock, DirectionLength>> {
    val neighbours = mutableListOf<Pair<CityBlock, DirectionLength>>()

    if (source.coord.x + 1 in graph.indices && currentDirectionLength.allowsDirection(Direction.SOUTH, 2)) {
        println("found neighbour SOUTH")
        neighbours.add(
            Pair(
                graph[source.coord.x + 1][source.coord.y],
                currentDirectionLength.addDirection(Direction.SOUTH)
            )
        )
    }
    if (source.coord.x - 1 in graph.indices && currentDirectionLength.allowsDirection(Direction.NORTH, 2)) {
        println("found neighbour NORTH")
        neighbours.add(
            Pair(
                graph[source.coord.x - 1][source.coord.y],
                currentDirectionLength.addDirection(Direction.NORTH)
            )
        )
    }
    if (source.coord.y + 1 in graph[source.coord.x].indices && currentDirectionLength.allowsDirection(
            Direction.EAST,
            2
        )
    ) {
        println("found neighbour EAST")
        neighbours.add(
            Pair(
                graph[source.coord.x][source.coord.y + 1],
                currentDirectionLength.addDirection(Direction.EAST)
            )
        )
    }
    if (source.coord.y - 1 in graph[source.coord.x].indices && currentDirectionLength.allowsDirection(
            Direction.WEST,
            2
        )
    ) {
        println("found neighbour WEST")
        neighbours.add(
            Pair(
                graph[source.coord.x][source.coord.y - 1],
                currentDirectionLength.addDirection(Direction.WEST)
            )
        )
    }

    return neighbours
}

private fun findAllNeighboursInDifferentDirection(
    source: CityBlock,
    currentDirectionLength: DirectionLength,
    minimumLength: Int,
    maximumLength: Int,
    graph: Grid
): MutableList<Pair<CityBlock, DirectionLength>> {
    val neighbours = mutableListOf<Pair<CityBlock, DirectionLength>>()

    if (currentDirectionLength.isTurnDirection(Direction.SOUTH)) {
        for (xOffset in minimumLength..maximumLength) {
            if (source.coord.x + xOffset in graph.indices) {
                neighbours.add(
                    Pair(
                        graph[source.coord.x + xOffset][source.coord.y],
                        DirectionLength(Direction.SOUTH, xOffset)
                    )
                )
            }
        }
    }
    if (currentDirectionLength.isTurnDirection(Direction.NORTH)) {
        for (xOffset in minimumLength..maximumLength) {
            if (source.coord.x - xOffset in graph.indices) {
                neighbours.add(
                    Pair(
                        graph[source.coord.x - xOffset][source.coord.y],
                        DirectionLength(Direction.NORTH, xOffset)
                    )
                )
            }
        }
    }
    if (currentDirectionLength.isTurnDirection(Direction.EAST)) {
        for (yOffset in minimumLength..maximumLength) {
            if (source.coord.y + yOffset in graph[source.coord.x].indices) {
                neighbours.add(
                    Pair(
                        graph[source.coord.x][source.coord.y + yOffset],
                        DirectionLength(Direction.EAST, yOffset)
                    )
                )
            }
        }
    }
    if (currentDirectionLength.isTurnDirection(Direction.WEST)) {
        for (yOffset in minimumLength..maximumLength) {
            if (source.coord.y - yOffset in graph[source.coord.x].indices) {
                neighbours.add(
                    Pair(
                        graph[source.coord.x][source.coord.y - yOffset],
                        DirectionLength(Direction.WEST, yOffset)
                    )
                )
            }
        }
    }

    return neighbours
}