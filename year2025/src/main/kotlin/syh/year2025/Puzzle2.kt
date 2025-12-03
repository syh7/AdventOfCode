package syh.year2025

import java.util.regex.Pattern
import syh.library.AbstractAocDay

class Puzzle2 : AbstractAocDay(2025, 2) {
    override fun doA(file: String): String {
        val ranges = readRanges(file)
        println(ranges)

        var totalInvalid = 0L

        for (range in ranges) {
            for (number in range) {
                val numberStr = number.toString()
                val length = numberStr.length
                val first = numberStr.substring(0..<length / 2)
                val second = numberStr.substring(length / 2)
                if (first == second) {
                    totalInvalid += number
                    println("found $number for total $totalInvalid")
                }
            }
        }

        return totalInvalid.toString()
    }

    override fun doB(file: String): String {
        val ranges = readRanges(file)
        println(ranges)

        val repeatedNumber = mutableSetOf<Long>()
        val pattern = Pattern.compile("^(\\d+)\\1+$")

        for (range in ranges) {
            for (number in range) {
                if (pattern.matcher(number.toString()).find()) {
                    repeatedNumber.add(number)
                    println("found $number")
                }
            }
        }

        return repeatedNumber.sum().toString()
    }

    private fun readRanges(file: String) = readSingleLineFile(file)
        .first()
        .split(",")
        .map { it.split("-").map { n -> n.toLong() } }
        .map { it[0]..it[1] }
}