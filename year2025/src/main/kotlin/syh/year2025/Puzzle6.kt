package syh.year2025

import syh.library.AbstractAocDay

class Puzzle6 : AbstractAocDay(2025, 6) {
    override fun doA(file: String): String {
        val splitLines = readSingleLineFile(file)
            .map { it.split(" ").filter { c -> c.isNotBlank() } }

        var total = 0L
        for (i in splitLines[0].indices) {
            val operator = splitLines.last()[i]
            val numbers = splitLines.map { it[i] }.dropLast(1).map { it.toLong() }
            val subtotal = performCalculation(operator, numbers)
            total += subtotal
        }
        return total.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)
        val maxlength = lines.maxOf { it.length }
        val normalizedLines = lines.map { it.padEnd(maxlength) }

        var total = 0L
        val numbers = mutableListOf<Long>()
        for (charIndex in normalizedLines[0].indices.reversed()) {
            val numberString = normalizedLines.map { it[charIndex] }.joinToString("").trim()
            if (numberString.isBlank()) {
                continue
            }
            if (numberString.last().digitToIntOrNull() == null) {
                // found operator
                val operator = numberString.last().toString()
                val number = numberString.dropLast(1).trim().toLong()
                println("read number $number")
                numbers.add(number)
                val subtotal = performCalculation(operator, numbers)
                total += subtotal
                println("calculated $subtotal for $operator with $numbers")
                numbers.clear()
            } else {
                val number = numberString.toLong()
                println("read number $number")
                numbers.add(number)
            }
        }

        return total.toString()
    }

    private fun performCalculation(operator: String, numbers: List<Long>): Long {
        return when (operator) {
            "*" -> numbers.reduce { acc, l -> acc * l }
            "+" -> numbers.reduce { acc, l -> acc + l }
            else -> throw IllegalArgumentException("unknown operator '$operator'")
        }
    }
}