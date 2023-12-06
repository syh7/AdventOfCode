package year2022.day3

import readFile

fun main() {
    val lines = readFile("year2022/day3/actual.txt")

    val total = lines.map { findDoubleItemType(it) }
        .sumOf { getPriority(it) }


    println("total: $total")
}

private fun findDoubleItemType(line: String): Char {
    val (first, second) = line.chunked(line.length / 2)
    val firstSet = first.toSet()
    val secondSet = second.toSet()
    val duplicateItems = firstSet.filter { secondSet.contains(it) }
    return duplicateItems[0]
}

private fun getPriority(c: Char): Int {
    return if (c.toString() in "A".."Z") {
        c.minus('A') + 1 + 26
    } else {
        c.minus('a') + 1
    }
}
