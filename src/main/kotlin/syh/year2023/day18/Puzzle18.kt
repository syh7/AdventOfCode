package syh.year2023.day18

import syh.AbstractAocDay
import java.util.function.Function
import kotlin.math.abs


class Puzzle18 : AbstractAocDay(2023, 18) {
    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)

        println("part a:")
        return calculateTotalArea(lines) { line -> lineParserPartA(line) }.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)

        println("part a:")
        return calculateTotalArea(lines) { line -> lineParserPartB(line) }.toString()
    }


    private fun lineParserPartA(line: String): Pair<String, Long> {
        val (direction, deltaStr, _) = line.split(" ")
        val delta = deltaStr.toLong()
        return direction to delta
    }

    private fun lineParserPartB(line: String): Pair<String, Long> {
        val (_, _, hexadecimal) = line.split(" ")
        val hexNumbers = hexadecimal.removePrefix("(#").removeSuffix(")")
        val distance = hexNumbers.substring(0, 5).toLong(16)
        val direction = when (hexNumbers[5].toString().toInt()) {
            0 -> "R"
            1 -> "D"
            2 -> "L"
            3 -> "U"
            else -> throw IllegalArgumentException("Invalid direction found in $hexadecimal")
        }

//    println("hexnumbers: $hexNumbers")
//    println("distance: ${hexNumbers.substring(0, 5)} becomes $distance")
//    println("direction: ${hexNumbers[5]} becomes $direction")

        return direction to distance
    }

    private fun calculateTotalArea(lines: List<String>, lineParser: Function<String, Pair<String, Long>>): Long {
        val coords = mutableListOf<Coord>()
        var totalTrenchLength = 0L

        var currentX = 0L
        var currentY = 0L

        for (line in lines) {
            val (direction, delta) = lineParser.apply(line)

            totalTrenchLength += delta
            when (direction) {
                "D" -> currentX += delta
                "U" -> currentX -= delta
                "L" -> currentY -= delta
                "R" -> currentY += delta
            }
            coords.add(Coord(currentX, currentY))
        }

        println("totalTrenchLength: $totalTrenchLength")
        val shoelaceArea = shoelaceArea(coords)
        println("shoelaceArea: $shoelaceArea")

        val actualArea = shoelaceArea + totalTrenchLength / 2 + 1
        println("actualArea: $actualArea")
        return actualArea
    }

    private fun shoelaceArea(coords: List<Coord>): Long {
        val n = coords.size
        var area = 0L
        for (i in 0..<n - 1) {
            area += coords[i].x * coords[i + 1].y - coords[i + 1].x * coords[i].y
        }
        // wrap around so last and first are multiplied
        area += coords[n - 1].x * coords[0].y - coords[0].x * coords[n - 1].y
        return abs(area) / 2L
    }

    data class Coord(val x: Long, val y: Long) {
        override fun toString(): String {
            return "[$x][$y]"
        }
    }
}