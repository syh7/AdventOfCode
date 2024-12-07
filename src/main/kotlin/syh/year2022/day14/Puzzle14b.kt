package syh.year2022.day14

import syh.readSingleLineFile

var yOffset = 0

fun main() {
    val walls = readSingleLineFile("year2022/day14/actual.txt").map { parseWall(it) }

    val minX = 0
    val maxX = walls.map { it.coords }.flatten().maxOf { it.x }

    val minY = walls.map { it.coords }.flatten().minOf { it.y }
    val maxY = walls.map { it.coords }.flatten().maxOf { it.y }

    println("minX: $minY")
    println("maxX: $maxX")
    println("minY: $minY")
    println("maxY: $maxY")

    val grid = MutableList(maxX - minX + 1) { MutableList(maxY - minY + 1) { EMPTY } }
    println("grid size: ${grid.size} x ${grid[0].size}")

    walls.forEach { wall ->
        val coords = wall.coords
        var i = 1
        while (i in coords.indices) {
            for (x in createMinMaxRange(coords[i - 1].x, coords[i].x)) {
                for (y in createMinMaxRange(coords[i - 1].y, coords[i].y)) {
                    grid[x - minX][y - minY] = WALL
                }
            }
            i++
        }
    }

    grid.add(MutableList(maxY - minY + 1) { EMPTY })
    grid.add(MutableList(maxY - minY + 1) { WALL })

    grid[0][500 - minY] = SAND_ORIGIN

    var steps = 0
    while (makeSandFall(grid, minY)) {
        steps++
    }

    grid.forEach { println(it.joinToString("") { itt -> itt }) }
    println("total steps: $steps")
}

private fun makeSandFall(grid: MutableList<MutableList<String>>, minY: Int): Boolean {
    var sandFall = true
    var x = 0
    var y = 500 - minY + yOffset
    while (sandFall) {
        if (x + 1 >= grid.size) {
            x += 1
            sandFall = false

        } else if (grid[x + 1][y] == EMPTY) {
            x += 1

        } else if (y - 1 < 0) {
            println("expanding left")
            grid.forEach { it.add(0, EMPTY) }
            grid.last()[0] = WALL

            // added a line in front, so we need to up our offsets
            y += 1
            yOffset++

        } else if (grid[x + 1][y - 1] == EMPTY) {
            x += 1
            y -= 1

        } else if (y + 1 >= grid[x].size) {
            println("expanding right")
            val size = grid[x].size
            grid.forEach { it.add(size, EMPTY) }
            grid.last()[size] = WALL

            // added line in end, no need to change offsets

        } else if (grid[x + 1][y + 1] == EMPTY) {
            x += 1
            y += 1

        } else {
            break
        }
    }

    if (!sandFall) {
        return false
    }

    return if (grid[x][y] == EMPTY || grid[x][y] == SAND_ORIGIN) {
        grid[x][y] = SAND
        true
    } else {
        false
    }
}

private fun createMinMaxRange(a: Int, b: Int): IntRange {
    val list = listOf(a, b)
    return list.min()..list.max()
}


