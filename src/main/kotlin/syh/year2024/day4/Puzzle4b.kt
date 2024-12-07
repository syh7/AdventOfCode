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

    val NE = Diagonal(1, 1)
    val SE = Diagonal(1, -1)
    val SW = Diagonal(-1, -1)
    val NW = Diagonal(-1, 1)

    if (startX in 0 until xSize
        && startY in 0 until ySize
        && map[startY][startX] == "A"
    ) {
        val neBool = checkMas(startX, startY, xSize, ySize, map, NE)
        val seBool = checkMas(startX, startY, xSize, ySize, map, SE)
        val swBool = checkMas(startX, startY, xSize, ySize, map, SW)
        val nwBool = checkMas(startX, startY, xSize, ySize, map, NW)
        if (
            (neBool && seBool)
            || (neBool && nwBool)
            || (seBool && swBool)
            || (swBool && nwBool)
        ) {
            xmasCount++
        }

    }
    return xmasCount
}

private fun checkMas(
    startX: Int,
    startY: Int,
    xSize: Int,
    ySize: Int,
    map: List<List<String>>,
    diagonal: Diagonal
): Boolean {
    val xOffset = diagonal.x
    val yOffset = diagonal.y
    return (startX + xOffset in 0 until xSize
            && startY + yOffset in 0 until ySize
            && map[startY + yOffset][startX + xOffset] == "M"

            && startX - xOffset in 0 until xSize
            && startY - yOffset in 0 until ySize
            && map[startY - yOffset][startX - xOffset] == "S")
}

private val directions: List<Diagonal> = listOf(
    Diagonal(1, 1),
    Diagonal(1, -1),
    Diagonal(-1, -1),
    Diagonal(-1, 1),
)

private data class Diagonal(val x: Int, val y: Int)
