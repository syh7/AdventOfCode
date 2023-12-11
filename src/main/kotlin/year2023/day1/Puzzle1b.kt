package year2023.day1

import readSingleLineFile

fun main() {
    val total = readSingleLineFile("year2023/day1/actual.txt")
        .map { convertString(it) }
        .map { it.filter { char -> char.isDigit() } }
        .map { it.first() + "" + it.last() }
        .sumOf { it.toInt() }
    println("total: $total")
}

private fun convertString(str: String): String {
    // cannot use replace("one", "1") because of cases like "oneight" which should translate to "18"
    return str.replace("one", "one1one")
        .replace("two", "two2two")
        .replace("three", "three3three")
        .replace("four", "four4four")
        .replace("five", "five5five")
        .replace("six", "six6six")
        .replace("seven", "seven7seven")
        .replace("eight", "eight8eight")
        .replace("nine", "nine9nine")
        .replace("zero", "zero0zero")
}