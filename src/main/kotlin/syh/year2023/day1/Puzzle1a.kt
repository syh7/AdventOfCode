package syh.year2023.day1

import syh.readSingleLineFile

fun main() {
    val total = readSingleLineFile("year2023/day1/actual.txt")
        .map { it.filter { char -> char.isDigit() } }
        .map { it.first() + "" + it.last() }
        .sumOf { it.toInt() }
    println("total: $total")
}