package syh.year2022.day4

import syh.readSingleLineFile

fun main() {
    val lines = readSingleLineFile("year2022/day4/actual.txt")

    val total = lines.map { it.split(",") }.count { split ->
        val firstRange = calculateRange(split[0])
        val secondRange = calculateRange(split[1])
        val contains = firstRange.overlaps(secondRange) || secondRange.overlaps(firstRange)
        println("$split contains each other $contains")
        contains
    }

    println("total: $total")
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