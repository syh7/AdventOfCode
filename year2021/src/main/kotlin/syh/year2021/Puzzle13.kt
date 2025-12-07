package syh.year2021

import syh.library.AbstractAocDay
import syh.library.Coord

class Puzzle13 : AbstractAocDay(2021, 13) {
    override fun doA(file: String): String {
        val (dotInstructions, foldInstructions) = readDoubleLineFile(file)
        val dots = dotInstructions.split("\r\n").map {
            val (x, y) = it.split(",")
            Coord(y.toInt(), x.toInt())
        }
            .toSet()

        val instructions = foldInstructions.split("\r\n")
            .map { it.removePrefix("fold along ") }
            .map {
                val (line, nr) = it.split("=")
                line to nr.toInt()
            }
            .first()

        val newCoords = fold(dots, instructions.first, instructions.second)

        return newCoords.size.toString()
    }

    override fun doB(file: String): String {
        val (dotInstructions, foldInstructions) = readDoubleLineFile(file)
        var dots = dotInstructions.split("\r\n").map {
            val (x, y) = it.split(",")
            Coord(y.toInt(), x.toInt())
        }
            .toSet()

        val instructions = foldInstructions.split("\r\n")
            .map { it.removePrefix("fold along ") }
            .map {
                val (line, nr) = it.split("=")
                line to nr.toInt()
            }

        instructions.forEach { (axis, value) ->
            dots = fold(dots, axis, value)
        }

        return print(dots)
    }

    private fun fold(coords: Set<Coord>, line: String, lineNr: Int): Set<Coord> {
        if (line == "y") {
            return coords.map { (row, column) ->
                if (row <= lineNr) {
                    Coord(row, column)
                } else {
                    val offset = row - lineNr
                    Coord(row - 2 * offset, column)
                }
            }.toSet()
        } else {
            return coords.map { (row, column) ->
                if (column <= lineNr) {
                    Coord(row, column)
                } else {
                    val offset = column - lineNr
                    Coord(row, column - 2 * offset)
                }
            }.toSet()
        }
    }

    private fun print(coords: Set<Coord>): String {
        var str = ""
        val maxX = coords.maxOf { it.column }
        val maxY = coords.maxOf { it.row }
        for (row in 0..maxY) {
            for (column in 0..maxX) {
                if (coords.contains(Coord(row, column))) {
                    print("#")
                    str += "#"
                } else {
                    print(".")
                    str += "."
                }
            }
            println()
            str += "\n"
        }
        return str
    }

}