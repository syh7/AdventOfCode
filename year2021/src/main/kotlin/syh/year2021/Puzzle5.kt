package syh.year2021

import syh.library.AbstractAocDay
import syh.library.Coord


class Puzzle5 : AbstractAocDay(2021, 5) {
    override fun doA(file: String): String {
        val coordLines = readSingleLineFile(file)
            .map { parseLine(it, false) }
            .filter { it.isNotEmpty() }

        for (coordLine in coordLines) {
            println(coordLine.map { it.toCoordString() })
        }


        val occurrencesMap = mutableMapOf<Coord, Int>()
        for (coord in coordLines.flatten()) {
            occurrencesMap.putIfAbsent(coord, 0)
            occurrencesMap[coord] = occurrencesMap[coord]!! + 1
        }

        occurrencesMap.entries.forEach { println(it) }

        return occurrencesMap.values.count { it > 1 }.toString()
    }

    override fun doB(file: String): String {
        val coordLines = readSingleLineFile(file)
            .map { parseLine(it, true) }
            .filter { it.isNotEmpty() }

        for (coordLine in coordLines) {
            println(coordLine.map { it.toCoordString() })
        }


        val occurrencesMap = mutableMapOf<Coord, Int>()
        for (coord in coordLines.flatten()) {
            occurrencesMap.putIfAbsent(coord, 0)
            occurrencesMap[coord] = occurrencesMap[coord]!! + 1
        }

        occurrencesMap.entries.forEach { println(it) }

        return occurrencesMap.values.count { it > 1 }.toString()
    }

    private fun parseLine(str: String, includeDiagonal: Boolean): List<Coord> {
        val (start, end) = str.split(" -> ")
        val (startX, startY) = start.split(",").map { it.toInt() }
        val (endX, endY) = end.split(",").map { it.toInt() }
        return if (startX == endX && startY != endY) {
            range(startY, endY).map { Coord(startX, it) }
        } else if (startX != endX && startY == endY) {
            range(startX, endX).map { Coord(it, startY) }
        } else {
            if (!includeDiagonal) {
                return emptyList()
            }
            val xCoords = range(startX, endX)
            val yCoords = range(startY, endY)
            xCoords.zip(yCoords) { x, y -> Coord(x, y) }
        }
    }

    private fun range(start: Int, end: Int): IntProgression {
        return if (start > end) {
            start downTo end
        } else {
            start..end
        }
    }
}