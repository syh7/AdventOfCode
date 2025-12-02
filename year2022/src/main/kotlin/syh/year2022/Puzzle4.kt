package syh.year2022

import syh.library.AbstractAocDay

class Puzzle4 : AbstractAocDay(2022, 4) {
    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)

        val total = lines.map { it.split(",") }
            .count { split ->
                val firstRange = calculateRange(split[0])
                val secondRange = calculateRange(split[1])
                val contains = firstRange.contains(secondRange) || secondRange.contains(firstRange)
                println("$split contains each other $contains")
                contains
            }

        println("total: $total")
        return total.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)

        val total = lines.map { it.split(",") }
            .count { split ->
                val firstRange = calculateRange(split[0])
                val secondRange = calculateRange(split[1])
                val overlaps = firstRange.overlaps(secondRange) || secondRange.overlaps(firstRange)
                println("$split overlaps $overlaps")
                overlaps
            }

        println("total: $total")
        return total.toString()
    }

    private fun calculateRange(str: String): IntRange {
        val (start, end) = str.split("-")
        return start.toInt()..end.toInt()
    }

    private fun IntRange.contains(other: IntRange): Boolean {
        return other.first >= this.first && other.last <= this.last
    }

    private fun IntRange.overlaps(other: IntRange): Boolean {
        return (other.first >= this.first && other.first <= this.last)
                || (other.last >= this.last && other.last <= this.first)
    }
}