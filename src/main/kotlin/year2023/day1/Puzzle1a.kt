package year2023.day1

import readFile

fun main() {
    val total = readFile("year2023/day1/actual.txt")
        .map { it.filter { char -> char.isDigit() } }
        .map { it.first() + "" + it.last() }
        .sumOf { it.toInt() }
    println("total: $total")
}