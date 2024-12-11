package syh.year2023.day1

import syh.AbstractAocDay

class Puzzle1 : AbstractAocDay(2023, 1) {
    override fun doA(file: String): String {
        val total = readSingleLineFile(file)
            .map { it.filter { char -> char.isDigit() } }
            .map { it.first() + "" + it.last() }
            .sumOf { it.toInt() }
        println("total: $total")
        return total.toString()
    }

    override fun doB(file: String): String {
        val total = readSingleLineFile(file)
            .map { convertString(it) }
            .map { it.filter { char -> char.isDigit() } }
            .map { it.first() + "" + it.last() }
            .sumOf { it.toInt() }
        println("total: $total")
        return total.toString()
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
}