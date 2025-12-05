package syh.year2025

import syh.library.AbstractAocDay
import syh.library.merge

class Puzzle5 : AbstractAocDay(2025, 5) {
    override fun doA(file: String): String {
        val (rangeLines, availableLines) = readDoubleLineFile(file)
        val ranges = rangeLines.split("\r\n")
            .map { line ->
                val (start, end) = line.split("-")
                start.toLong()..end.toLong()
            }
        val ingredients = availableLines.split("\r\n").map { it.toLong() }

        return ingredients.count { ranges.any { range -> range.contains(it) } }.toString()

    }

    override fun doB(file: String): String {
        val (rangeLines, _) = readDoubleLineFile(file)
        val ranges = rangeLines.split("\r\n")
            .map { line ->
                val (start, end) = line.split("-")
                start.toLong()..end.toLong()
            }
            .sortedBy { it.first }
        println("read ${ranges.size} ranges $ranges")

        val combinedRanges = ranges.merge()
        println("leftover ranges ${combinedRanges.size}: $combinedRanges")

        return combinedRanges.map { it.last - it.first + 1 }.also { println(it) }.sum().toString()
    }
}