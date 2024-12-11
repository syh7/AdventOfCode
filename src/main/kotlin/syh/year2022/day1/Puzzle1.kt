package syh.year2022.day1

import syh.AbstractAocDay

class Puzzle1 : AbstractAocDay(2022, 1) {
    override fun doA(file: String): String {
        val elves = readDoubleLineFile(file)
            .map { it.split("\r\n").map { line -> line.toIntOrNull() } }
            .map { list -> list.sumOf { it ?: 0 } }
        return elves.max().toString()
    }

    override fun doB(file: String): String {
        val elves = readDoubleLineFile(file)
            .map { it.split("\r\n").map { line -> line.toIntOrNull() } }
            .map { list -> list.sumOf { it ?: 0 } }
            .sortedDescending()
        return elves.take(3).sum().toString()
    }

}