package syh.year2024.day4

import syh.readSingleLineFile

fun main() {

    val chars: List<List<String>> = readSingleLineFile("year2024/day4/actual.txt")
        .map { it.split("") }

    var totalXmasCount = 0
    for (x in chars[0].indices) {
        for (y in chars.indices) {
            val xmasCount = findXmas(x, y, chars)
            println("XMAS count for x=$x,y=$y is $xmasCount")
            totalXmasCount += xmasCount
        }
    }
    println("total = $totalXmasCount")
}

private fun findXmas(startX: Int, startY: Int, map: List<List<String>>): Int {
    val xSize = map[0].size
    val ySize = map.size

    var xmasCount = 0

    for ((xOffset, yOffset) in directions) {
        if (startX in 0 until xSize
            && startY in 0 until ySize
            && map[startY][startX] == "X"
        ) {
            if (startX + xOffset in 0 until xSize
                && startY + yOffset in 0 until ySize
                && map[startY + yOffset][startX + xOffset] == "M"
            ) {
                if (startX + xOffset * 2 in 0 until xSize
                    && startY + yOffset * 2 in 0 until ySize
                    && map[startY + yOffset * 2][startX + xOffset * 2] == "A"
                ) {
                    if (startX + xOffset * 3 in 0 until xSize
                        && startY + yOffset * 3 in 0 until ySize
                        && map[startY + yOffset * 3][startX + xOffset * 3] == "S"
                    ) {
                        xmasCount++
                    }

                }
            }
        }
    }
    return xmasCount
}

private val directions: List<Direction> = listOf(
    Direction(1, 1),
    Direction(1, 0),
    Direction(1, -1),
    Direction(0, -1),
    Direction(-1, -1),
    Direction(-1, 0),
    Direction(-1, 1),
    Direction(0, 1),
)

private data class Direction(val x: Int, val y: Int)
