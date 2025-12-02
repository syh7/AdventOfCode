package syh.year2023

import syh.library.AbstractAocDay

class Puzzle10 : AbstractAocDay(2023, 10) {
    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)

        val grid: MutableList<MutableList<Pipe>> = mutableListOf()
        for (i in lines.indices) {
            val split = lines[i].split("").filter { it.isNotEmpty() }
            val row = mutableListOf<Pipe>()
            for (j in split.indices) {
                row.add(Pipe(i, j, pipeTypeFromString(split[j])))
            }
            grid.add(row)
        }

        val circle = findCircle(grid)

        val maxDistancePipe = circle.maxBy { it.distance }

        grid.forEach { it.prettyPrint() }

        println("pipe that is max distance from the start: $maxDistancePipe")
        return maxDistancePipe.distance.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)

        val grid: MutableList<MutableList<Pipe>> = mutableListOf()
        for (i in lines.indices) {
            val split = lines[i].split("").filter { it.isNotEmpty() }
            val row = mutableListOf<Pipe>()
            for (j in split.indices) {
                row.add(Pipe(i, j, pipeTypeFromString(split[j])))
            }
            grid.add(row)
        }

        val circle = findCircle(grid)

        val maxDistancePipe = circle.maxBy { it.distance }

        val enclosedTiles = countEnclosedTiles(grid, circle)

        grid.forEach { it.prettyPrint() }

        println("pipe that is max distance from the start: $maxDistancePipe")
        println("number of tiles enclosed by the circle: $enclosedTiles")
        return enclosedTiles.toString()
    }

    private fun countEnclosedTiles(grid: MutableList<MutableList<Pipe>>, circle: List<Pipe>): Int {
        var enclosedTiles = 0

        // count number of enclosed tiles per line
        // it is enclosed if there is a circle-tile before and after itself
        // we can't depend on total number of circle-tiles before current tile because that includes horizontal tiles
        // but we can depend on number of vertical tiles
        // if we have two corners that combined go north-south (e.g. L7 or L----7), that is also a vertical tile
        // so if we have an uneven number of vertical circle-tiles before the current non-circle-tile, then that is an enclosed tile
        for (i in grid.indices) {

            // initialize for this line
            var crosses = 0
            var previousCircleTileType = PipeType.GROUND

            for (j in grid[i].indices) {
                val tile = grid[i][j]

                if (circle.contains(tile)) {
                    // regular vertical bar
                    if (tile.type == PipeType.NORTH_SOUTH) {
                        crosses++

                    } else if (tile.type != PipeType.EAST_WEST) {
                        // if we have a corner combination, up crosses
                        if (previousCircleTileType != PipeType.GROUND) {
                            if (isCorner(previousCircleTileType, tile.type)) {
                                crosses++
                            }
                            previousCircleTileType = PipeType.GROUND

                        } else {
                            previousCircleTileType = tile.type
                        }
                    }

                } else if (crosses % 2 == 1) {
                    enclosedTiles++
                    tile.type = PipeType.ENCLOSED
                } else {
                    tile.type = PipeType.NOT_ENCLOSED
                }
            }
        }

        return enclosedTiles
    }

    private fun isCorner(a: PipeType, b: PipeType): Boolean {
        return (a == PipeType.SOUTH_EAST && b == PipeType.NORTH_WEST)
                || (a == PipeType.SOUTH_WEST && b == PipeType.NORTH_EAST)
                || (a == PipeType.NORTH_WEST && b == PipeType.SOUTH_EAST)
                || (a == PipeType.NORTH_EAST && b == PipeType.SOUTH_WEST)
    }

    private fun findCircle(grid: MutableList<MutableList<Pipe>>): List<Pipe> {
        val startPipe = grid.flatten().first { it.type == PipeType.START }
        println("start pipe: ${startPipe.toCoordString()}")

        val path = mutableListOf<Pipe>()
        startPipe.distance = 0

        var currentPipe: Pipe
        val unvisited = mutableListOf(startPipe)

        while (unvisited.isNotEmpty()) {
            currentPipe = unvisited.first()
            unvisited.remove(currentPipe)
            path.add(currentPipe)

            val neighbours = findNeighbourForPipe(currentPipe, grid)
            val unvisitedNeighbours = neighbours.filterNot { path.contains(it) }
            val neighboursToVisit = unvisitedNeighbours.filterNot { unvisited.contains(it) }
            neighboursToVisit.forEach { unvisited.add(it) }

            val newDistance = currentPipe.distance + 1
            unvisitedNeighbours.forEach { neighbour ->
                if (neighbour.distance > newDistance) {
                    neighbour.distance = newDistance
                    neighbour.previous = currentPipe
                }
            }
        }

        return path
    }

    private fun findNeighbourForPipe(origin: Pipe, grid: MutableList<MutableList<Pipe>>): List<Pipe> {
        return when (origin.type) {
            PipeType.NORTH_SOUTH -> listOf(grid[origin.x - 1][origin.y], grid[origin.x + 1][origin.y])
            PipeType.EAST_WEST -> listOf(grid[origin.x][origin.y + 1], grid[origin.x][origin.y - 1])
            PipeType.NORTH_EAST -> listOf(grid[origin.x - 1][origin.y], grid[origin.x][origin.y + 1])
            PipeType.NORTH_WEST -> listOf(grid[origin.x - 1][origin.y], grid[origin.x][origin.y - 1])
            PipeType.SOUTH_EAST -> listOf(grid[origin.x + 1][origin.y], grid[origin.x][origin.y + 1])
            PipeType.SOUTH_WEST -> listOf(grid[origin.x + 1][origin.y], grid[origin.x][origin.y - 1])
            PipeType.START -> findNeighboursForStart(origin, grid)
            PipeType.GROUND -> throw IllegalArgumentException("${origin.toCoordString()} does not have neighbours because it is not a pipe")
            PipeType.ENCLOSED -> throw IllegalArgumentException("${origin.toCoordString()} does not have neighbours because it is not a pipe")
            PipeType.NOT_ENCLOSED -> throw IllegalArgumentException("${origin.toCoordString()} does not have neighbours because it is not a pipe")
        }
    }

    private fun findNeighboursForStart(startPipe: Pipe, grid: MutableList<MutableList<Pipe>>): List<Pipe> {
        val neighbours = mutableListOf<Pipe>()

        val possibleNorthPipeTypes = listOf(PipeType.NORTH_SOUTH, PipeType.SOUTH_WEST, PipeType.SOUTH_EAST)
        val possibleSouthPipeTypes = listOf(PipeType.NORTH_SOUTH, PipeType.NORTH_WEST, PipeType.NORTH_EAST)
        val possibleWestPipeTypes = listOf(PipeType.EAST_WEST, PipeType.SOUTH_EAST, PipeType.NORTH_EAST)
        val possibleEastPipeTypes = listOf(PipeType.EAST_WEST, PipeType.NORTH_WEST, PipeType.SOUTH_WEST)

        // check south
        if (startPipe.x + 1 in grid.indices) {
            val possibleNeighbour = grid[startPipe.x + 1][startPipe.y]
            if (possibleNeighbour.type in possibleSouthPipeTypes) {
                neighbours.add(possibleNeighbour)
            }
        }
        // check north
        if (startPipe.x - 1 in grid.indices) {
            val possibleNeighbour = grid[startPipe.x - 1][startPipe.y]
            if (possibleNeighbour.type in possibleNorthPipeTypes) {
                neighbours.add(possibleNeighbour)
            }
        }
        // check east
        if (startPipe.y + 1 in grid[startPipe.x].indices) {
            val possibleNeighbour = grid[startPipe.x][startPipe.y + 1]
            if (possibleNeighbour.type in possibleEastPipeTypes) {
                neighbours.add(possibleNeighbour)
            }
        }
        // check west
        if (startPipe.y - 1 in grid[startPipe.x].indices) {
            val possibleNeighbour = grid[startPipe.x][startPipe.y - 1]
            if (possibleNeighbour.type in possibleWestPipeTypes) {
                neighbours.add(possibleNeighbour)
            }
        }
        return neighbours
    }

    enum class PipeType(val stringValue: String) {
        NORTH_SOUTH("|"),
        EAST_WEST("-"),
        NORTH_EAST("L"),
        NORTH_WEST("J"),
        SOUTH_WEST("7"),
        SOUTH_EAST("F"),
        GROUND("."),
        START("S"),
        ENCLOSED("X"),
        NOT_ENCLOSED(" ");
    }

    private fun pipeTypeFromString(str: String): PipeType {
        return PipeType.entries.first { it.stringValue == str }
    }

    data class Pipe(
        val x: Int, val y: Int, var type: PipeType, var previous: Pipe? = null, var distance: Int = Int.MAX_VALUE
    ) {
        override fun toString(): String {
            val previousStr =
                if (previous != null) "with previous [${previous!!.x}][${previous!!.y}]" else "without previous"
            return "[$x][$y], distance $distance, $previousStr"
        }

        fun toCoordString(): String {
            return "[$x][$y]"
        }
    }

    private fun List<Pipe>.prettyPrint() {
        println(this.map { it.type.stringValue }.joinToString("") { it })
    }
}

