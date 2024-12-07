package syh.year2023.day16

import syh.readSingleLineFile

const val EMPTY = "."
const val MIRROR_SLASH = "/"
const val MIRROR_BACKSLASH = "\\"
const val MIRROR_EAST_WEST = "-"
const val MIRROR_NORTH_SOUTH = "|"

data class CaveFloor(
    val type: String,
    val coord: Coord,
    val energyDirections: MutableList<String> = mutableListOf(),
    var energized: Boolean = false
) {
    override fun toString(): String {
        return if (type == EMPTY) {
            if (energized) "#" else EMPTY
        } else {
            type
        }
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

typealias Grid = MutableList<MutableList<CaveFloor>>

private fun MutableList<MutableList<CaveFloor>>.prettyPrint() {
    this.forEach { line -> println(line.joinToString("") { it.toString() }) }
}

fun main() {
    val lines = readSingleLineFile("year2023/day16/actual.txt")

    val grid: Grid = mutableListOf()

    for (i in lines.indices) {
        val row = mutableListOf<CaveFloor>()
        val split = lines[i].split("").filter { it.isNotBlank() }
        for (j in split.indices) {
            row.add(CaveFloor(type = split[j], coord = Coord(i, j)))
        }
        grid.add(row)
    }

    grid.prettyPrint()

    var maxEnergized = 0
    for (x in grid.indices) {
        var gridClone = cloneGrid(grid)
        traverseEnergyPath(gridClone, Pair(gridClone[x][0], "EAST"))
        var energizedCells = countEnergizedCells(gridClone)
//        println("calculated $energizedCells for start [$x][0] going EAST")
        if (energizedCells > maxEnergized) {
            maxEnergized = energizedCells
        }

        gridClone = cloneGrid(grid)
        traverseEnergyPath(gridClone, Pair(gridClone[x][gridClone[0].size - 1], "WEST"))
//        println("calculated $energizedCells for start [$x][${gridClone[0].size - 1}] going WEST")
        energizedCells = countEnergizedCells(gridClone)
        if (energizedCells > maxEnergized) {
            maxEnergized = energizedCells
        }
    }
    for (y in grid[0].indices) {
        var gridClone = cloneGrid(grid)
        traverseEnergyPath(gridClone, Pair(gridClone[0][y], "SOUTH"))
        var energizedCells = countEnergizedCells(gridClone)
        println("calculated $energizedCells for start [0][$y] going SOUTH")
        if (energizedCells > maxEnergized) {
            maxEnergized = energizedCells
        }

        gridClone = cloneGrid(grid)
        traverseEnergyPath(gridClone, Pair(gridClone[grid.size - 1][y], "NORTH"))
        println("calculated $energizedCells for start [${grid.size - 1}][$y] going NORTH")
        energizedCells = countEnergizedCells(gridClone)
        if (energizedCells > maxEnergized) {
            maxEnergized = energizedCells
        }
    }


//    grid.prettyPrint()

    traverseEnergyPath(grid, Pair(grid[0][0], "EAST"))
    val totalEnergizedPartA = countEnergizedCells(grid)
    println("total energized for part A = $totalEnergizedPartA")
    println("total energized for part B = $maxEnergized")

}

private fun countEnergizedCells(grid: Grid) = grid.sumOf { row -> row.count { it.energized } }

private fun cloneGrid(grid: Grid): Grid {
    val newGrid = mutableListOf<MutableList<CaveFloor>>()
    for (currentRow in grid) {
        val newRow = mutableListOf<CaveFloor>()
        for (floor in currentRow) {
            newRow.add(CaveFloor(type = floor.type, coord = floor.coord))
        }
        newGrid.add(newRow)
    }
    return newGrid
}

private fun traverseEnergyPath(grid: Grid, startPair: Pair<CaveFloor, String>) {

    val floorToHandle = mutableListOf(startPair)

    while (floorToHandle.isNotEmpty()) {

        val current = floorToHandle.first()
        floorToHandle.remove(current)

        val currentFloor = current.first
        val currentDirection = current.second

//        println("Handling ${currentFloor.toCoordString()} with type ${currentFloor.type} in direction $currentDirection")

        if (currentFloor.energyDirections.contains(currentDirection)) {
//            println("Previously handled ${currentFloor.toCoordString()} with type ${currentFloor.type} in direction $currentDirection")
            continue
        }

        currentFloor.energized = true
        currentFloor.energyDirections.add(currentDirection)

        val neighbours = findNeighbours(currentFloor, currentDirection, grid)
        floorToHandle.addAll(neighbours)

//        println("found ${neighbours.size} neighbours")
//        neighbours.forEach { println("found neighbour ${it.first.toCoordString()} with type ${it.first.type} in direction ${it.second}") }
//        println()

    }
}

private fun findNeighbours(caveFloor: CaveFloor, originDirection: String, grid: Grid): List<Pair<CaveFloor, String>> {
    return when (caveFloor.type) {
        EMPTY -> findNeighbourForEmptyCaveFloor(caveFloor, originDirection, grid)
        MIRROR_EAST_WEST -> findNeighbourForEastWestMirror(caveFloor, originDirection, grid)
        MIRROR_NORTH_SOUTH -> findNeighbourForNorthSouthMirror(caveFloor, originDirection, grid)
        MIRROR_SLASH -> findNeighbourForSlashMirror(caveFloor, originDirection, grid)
        MIRROR_BACKSLASH -> findNeighbourForBackSlashMirror(caveFloor, originDirection, grid)

        else -> throw IllegalArgumentException("unexpected type: ${caveFloor.type}")
    }
}

private fun findNeighbourForEmptyCaveFloor(
    caveFloor: CaveFloor,
    originDirection: String,
    grid: Grid
): List<Pair<CaveFloor, String>> {
    // no mirror = "."
    if (originDirection == "EAST") {
        return if (caveFloor.coord.y + 1 < grid[0].size) {
            listOf(Pair(grid[caveFloor.coord.x][caveFloor.coord.y + 1], "EAST"))
        } else {
            emptyList()
        }
    }
    if (originDirection == "WEST") {
        return if (caveFloor.coord.y - 1 >= 0) {
            listOf(Pair(grid[caveFloor.coord.x][caveFloor.coord.y - 1], "WEST"))
        } else {
            emptyList()
        }
    }
    if (originDirection == "NORTH") {
        return if (caveFloor.coord.x - 1 >= 0) {
            listOf(Pair(grid[caveFloor.coord.x - 1][caveFloor.coord.y], "NORTH"))
        } else {
            emptyList()
        }
    }
    if (originDirection == "SOUTH") {
        return if (caveFloor.coord.x + 1 < grid.size) {
            listOf(Pair(grid[caveFloor.coord.x + 1][caveFloor.coord.y], "SOUTH"))
        } else {
            emptyList()
        }
    }
    throw IllegalStateException("unexpected direction $originDirection")
}

private fun findNeighbourForEastWestMirror(
    caveFloor: CaveFloor,
    originDirection: String,
    grid: Grid
): List<Pair<CaveFloor, String>> {
    // mirror = '-'
    if (originDirection == "EAST") {
        return if (caveFloor.coord.y + 1 < grid[0].size) {
            listOf(Pair(grid[caveFloor.coord.x][caveFloor.coord.y + 1], "EAST"))
        } else {
            emptyList()
        }
    }
    if (originDirection == "WEST") {
        return if (caveFloor.coord.y - 1 >= 0) {
            listOf(Pair(grid[caveFloor.coord.x][caveFloor.coord.y - 1], "WEST"))
        } else {
            emptyList()
        }
    }
    if (originDirection == "NORTH" || originDirection == "SOUTH") {
        val neighbours = mutableListOf<Pair<CaveFloor, String>>()
        if (caveFloor.coord.y - 1 >= 0) {
            neighbours.add(Pair(grid[caveFloor.coord.x][caveFloor.coord.y - 1], "WEST"))
        }
        if (caveFloor.coord.y + 1 < grid[0].size) {
            neighbours.add(Pair(grid[caveFloor.coord.x][caveFloor.coord.y + 1], "EAST"))
        }
        return neighbours
    }
    throw IllegalStateException("unexpected direction $originDirection")
}

private fun findNeighbourForNorthSouthMirror(
    caveFloor: CaveFloor,
    originDirection: String,
    grid: Grid
): List<Pair<CaveFloor, String>> {
    // mirror = '|'
    if (originDirection == "NORTH") {
        return if (caveFloor.coord.x - 1 >= 0) {
            listOf(Pair(grid[caveFloor.coord.x - 1][caveFloor.coord.y], "NORTH"))
        } else {
            emptyList()
        }
    }
    if (originDirection == "SOUTH") {
        return if (caveFloor.coord.x + 1 < grid.size) {
            listOf(Pair(grid[caveFloor.coord.x + 1][caveFloor.coord.y], "SOUTH"))
        } else {
            emptyList()
        }
    }
    if (originDirection == "EAST" || originDirection == "WEST") {
        val neighbours = mutableListOf<Pair<CaveFloor, String>>()
        if (caveFloor.coord.x - 1 >= 0) {
            neighbours.add(Pair(grid[caveFloor.coord.x - 1][caveFloor.coord.y], "NORTH"))
        }
        if (caveFloor.coord.x + 1 < grid.size) {
            neighbours.add(Pair(grid[caveFloor.coord.x + 1][caveFloor.coord.y], "SOUTH"))
        }
        return neighbours
    }
    throw IllegalStateException("unexpected direction $originDirection")
}


private fun findNeighbourForSlashMirror(
    caveFloor: CaveFloor,
    originDirection: String,
    grid: Grid
): List<Pair<CaveFloor, String>> {
    // mirror = '/'
    if (originDirection == "NORTH") {
        return if (caveFloor.coord.y + 1 < grid[0].size) {
            listOf(Pair(grid[caveFloor.coord.x][caveFloor.coord.y + 1], "EAST"))
        } else {
            emptyList()
        }
    }
    if (originDirection == "SOUTH") {
        return if (caveFloor.coord.y - 1 >= 0) {
            listOf(Pair(grid[caveFloor.coord.x][caveFloor.coord.y - 1], "WEST"))
        } else {
            emptyList()
        }
    }
    if (originDirection == "EAST") {
        return if (caveFloor.coord.x - 1 >= 0) {
            listOf(Pair(grid[caveFloor.coord.x - 1][caveFloor.coord.y], "NORTH"))
        } else {
            emptyList()
        }
    }
    if (originDirection == "WEST") {
        return if (caveFloor.coord.x + 1 < grid.size) {
            listOf(Pair(grid[caveFloor.coord.x + 1][caveFloor.coord.y], "SOUTH"))
        } else {
            emptyList()
        }
    }
    throw IllegalStateException("unexpected direction $originDirection")
}


private fun findNeighbourForBackSlashMirror(
    caveFloor: CaveFloor,
    originDirection: String,
    grid: Grid
): List<Pair<CaveFloor, String>> {
    // mirror = '\'
    if (originDirection == "NORTH") {
        return if (caveFloor.coord.y - 1 >= 0) {
            listOf(Pair(grid[caveFloor.coord.x][caveFloor.coord.y - 1], "WEST"))
        } else {
            emptyList()
        }
    }
    if (originDirection == "SOUTH") {
        return if (caveFloor.coord.y + 1 < grid[0].size) {
            listOf(Pair(grid[caveFloor.coord.x][caveFloor.coord.y + 1], "EAST"))
        } else {
            emptyList()
        }
    }
    if (originDirection == "EAST") {
        return if (caveFloor.coord.x + 1 < grid.size) {
            listOf(Pair(grid[caveFloor.coord.x + 1][caveFloor.coord.y], "SOUTH"))
        } else {
            emptyList()
        }
    }
    if (originDirection == "WEST") {
        return if (caveFloor.coord.x - 1 >= 0) {
            listOf(Pair(grid[caveFloor.coord.x - 1][caveFloor.coord.y], "NORTH"))
        } else {
            emptyList()
        }
    }
    throw IllegalStateException("unexpected direction $originDirection")
}