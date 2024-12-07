package syh.year2024.day4

import syh.AbstractAocDay

class Puzzle4 : AbstractAocDay(2024, 4) {
    override fun doA(file: String): Long {
        val chars: List<List<String>> = readSingleLineFile(file)
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
        return totalXmasCount.toLong()
    }

    override fun doB(file: String): Long {
        val chars: List<List<String>> = readSingleLineFile(file)
            .map { it.split("") }

        var totalXmasCount = 0
        for (x in chars[0].indices) {
            for (y in chars.indices) {
                val xmasCount = findCross(x, y, chars)
                println("XMAS count for x=$x,y=$y is $xmasCount")
                totalXmasCount += xmasCount
            }
        }
        println("total = $totalXmasCount")
        return totalXmasCount.toLong()
    }


    private fun findXmas(startX: Int, startY: Int, map: List<List<String>>): Int {
        var xmasCount = 0

        for ((xOffset, yOffset) in directions) {
            if (checkValue(startX, startY, xOffset, yOffset, 0, "X", map)
                && checkValue(startX, startY, xOffset, yOffset, 1, "M", map)
                && checkValue(startX, startY, xOffset, yOffset, 2, "A", map)
                && checkValue(startX, startY, xOffset, yOffset, 3, "S", map)
            ) {
                xmasCount++
            }
        }
        return xmasCount
    }

    private fun checkValue(
        startX: Int,
        startY: Int,
        xOffset: Int,
        yOffset: Int,
        offsetMultiplier: Int,
        expectedLetter: String,
        map: List<List<String>>,
    ): Boolean {
        val calcX = startX + xOffset * offsetMultiplier
        val calcY = startY + yOffset * offsetMultiplier
        return calcX in map[0].indices && calcY in map.indices && map[calcY][calcX] == expectedLetter
    }

    private fun findCross(startX: Int, startY: Int, map: List<List<String>>): Int {
        val xSize = map[0].size
        val ySize = map.size

        var xmasCount = 0

        val NE = Direction(1, 1)
        val SE = Direction(1, -1)
        val SW = Direction(-1, -1)
        val NW = Direction(-1, 1)

        if (startX in 0..<xSize
            && startY in 0..<ySize
            && map[startY][startX] == "A"
        ) {
            val neBool = checkMas(startX, startY, map, NE)
            val seBool = checkMas(startX, startY, map, SE)
            val swBool = checkMas(startX, startY, map, SW)
            val nwBool = checkMas(startX, startY, map, NW)
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
        map: List<List<String>>,
        diagonal: Direction
    ): Boolean {
        val xOffset = diagonal.x
        val yOffset = diagonal.y
        return checkValue(startX, startY, xOffset, yOffset, 1, "M", map)
                && checkValue(startX, startY, xOffset, yOffset, -1, "S", map)
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
}
