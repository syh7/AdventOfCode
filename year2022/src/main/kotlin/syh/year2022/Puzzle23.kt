package syh.year2022

import syh.library.AbstractAocDay
import syh.library.Coord
import syh.library.Direction


class Puzzle23 : AbstractAocDay(2022, 23) {

    override fun doA(file: String): String {
        val lines = readSingleLineFile(file).map { it.split("").filter { s -> s.isNotEmpty() } }
        val elves = lines
            .mapIndexed { row, line ->
                line.mapIndexedNotNull { column, place -> if (place == "#") Coord(row, column) else null }
            }
            .flatten()
            .toMutableSet()
        println("read elves:")
        println(elves)

        for (t in 0..<10) {
            val proposedChanges = calculateChanges(t, elves)
            for ((newPos, oldPos) in proposedChanges) {
                elves.remove(oldPos)
                elves.add(newPos)
            }
        }
        println(elves)
        printElves(elves)
        val maxRow = elves.maxOf { it.row }
        val minRow = elves.minOf { it.row }
        val maxColumn = elves.maxOf { it.column }
        val minColumn = elves.minOf { it.column }
        val totalField = (maxRow - minRow + 1) * (maxColumn - minColumn + 1)
        val result = totalField - elves.size
        println("maxRow:     $maxRow")
        println("minRow:     $minRow")
        println("maxColumn:  $maxColumn")
        println("minColumn:  $minColumn")
        println("totalField: $totalField")
        println("result:     $result")

        return result.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file).map { it.split("").filter { s -> s.isNotEmpty() } }
        val elves = lines
            .mapIndexed { row, line ->
                line.mapIndexedNotNull { column, place -> if (place == "#") Coord(row, column) else null }
            }
            .flatten()
            .toMutableSet()
        println("read elves:")
        println(elves)

        var t = 0
        while (true) {
            val proposedChanges = calculateChanges(t, elves)
            for ((newPos, oldPos) in proposedChanges) {
                elves.remove(oldPos)
                elves.add(newPos)
            }

            t += 1
            if (proposedChanges.isEmpty()) {
                break
            }
        }
        println(elves)
        printElves(elves)

        return t.toString()
    }

    private fun calculateChanges(t: Int, elves: MutableSet<Coord>): MutableMap<Coord, Coord> {
        val proposedChanges = mutableMapOf<Coord, Coord>()
        for (elf in elves) {
            val possibleNeighbour = Direction.ALL_DIRECTIONS.map { elf.relative(it) }.map { it in elves }

            if (possibleNeighbour.all { !it }) {
                // no need to move
                continue
            }

            // calculate possible positions
            val possibleMoves = listOf(
                (possibleNeighbour[7] || possibleNeighbour[0] || possibleNeighbour[1]) to elf.relative(Direction.NORTH),
                (possibleNeighbour[3] || possibleNeighbour[4] || possibleNeighbour[5]) to elf.relative(Direction.SOUTH),
                (possibleNeighbour[5] || possibleNeighbour[6] || possibleNeighbour[7]) to elf.relative(Direction.WEST),
                (possibleNeighbour[1] || possibleNeighbour[2] || possibleNeighbour[3]) to elf.relative(Direction.EAST),
            )
            // for the first open position in the preferred direction order
            // if the spot is free, propose this elf and break
            // if the spot is taken, remove the proposal and break
            for (i in 0..<4) {
                val (taken, newPos) = possibleMoves[(t + i) % 4]
                if (!taken) {
//                    println("elf $elf can move to $newPos")
                    if (proposedChanges.containsKey(newPos)) {
//                        println("except it is already taken by another elf")
                        proposedChanges.remove(newPos)
                    } else {
                        proposedChanges[newPos] = elf
                    }
                    break
                }
            }
        }
        return proposedChanges
    }

    private fun printElves(elves: Set<Coord>) {
        val maxRow = elves.maxOf { it.row }
        val minRow = elves.minOf { it.row }
        val maxColumn = elves.maxOf { it.column }
        val minColumn = elves.minOf { it.column }
        println("elves:")
        for (row in minRow..maxRow) {
            for (column in minColumn..maxColumn) {
                if (Coord(row, column) in elves) print("#") else print(".")
            }
            println()
        }

    }
}