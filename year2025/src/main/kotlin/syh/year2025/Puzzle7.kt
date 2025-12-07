package syh.year2025

import syh.library.AbstractAocDay
import syh.library.Coord
import syh.library.Grid

class Puzzle7 : AbstractAocDay(2025, 7) {
    override fun doA(file: String): String {
        val input = readSingleLineFile(file).map { it.split("").filter { c -> c.isNotEmpty() } }
        val grid = Grid<String>()
        grid.create(input) { it }

        val start = grid.locationOf("S")[0]
        var beams = mutableSetOf(start.first.column)
        var splitCounter = 0

        for (row in grid.grid.indices) {
            val newBeams = mutableSetOf<Int>()
            for (beam in beams) {

                val nextBeam = grid.at(Coord(row, beam))
                if (nextBeam == "^") {
                    newBeams.add(beam - 1)
                    newBeams.add(beam + 1)
                    newBeams.remove(beam)
                    splitCounter += 1
                } else {
                    newBeams.add(beam)
                }

            }
            beams = newBeams
        }

        return splitCounter.toString()
    }

    override fun doB(file: String): String {
        val input = readSingleLineFile(file).map { it.split("").filter { c -> c.isNotEmpty() } }
        val grid = Grid<String>()
        grid.create(input) { it }

        val start = grid.locationOf("S")[0]
        var beams = mutableMapOf(start.first.column to 1L)

        for (row in grid.grid.indices) {
            val newBeams = mutableMapOf<Int, Long>()
            for ((column, timelineValue) in beams) {

                val coord = Coord(row, column)
                val nextBeam = grid.at(coord)

                if (nextBeam == "^") {
                    println("${coord.toCoordString()} is a splitter with value $timelineValue")

                    newBeams.merge(column - 1, timelineValue) { prev, next -> prev + next }
                    newBeams.merge(column + 1, timelineValue) { prev, next -> prev + next }

                } else {

                    newBeams.merge(column, timelineValue) { prev, next -> prev + next }

                }

                grid.set(coord, timelineValue.toString())

            }
            beams = newBeams
        }

        grid.printValues()
        println("found ${beams.size} beams at the end")
        beams.forEach { (col, value) -> println("beam $col has value $value") }
        return beams.values.sum().toString()
    }
}