package syh.year2021

import syh.library.AbstractAocDay

class Puzzle10 : AbstractAocDay(2021, 10) {
    private val openingEndingPairs = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>',
    )

    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)
        return lines.map { getCorruptedValue(it) }
            .filter { it != 0 }
            .sum()
            .toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)
        val incompleteValues = lines.filter { getCorruptedValue(it) == 0 }
            .map { getIncompleteValue(it) }
            .sorted()
        println(incompleteValues)
        println(incompleteValues.size)
        val index = incompleteValues.size / 2
        println(index)
        return incompleteValues[index].toString()
    }

    private fun getCorruptedValue(str: String): Int {
        val chars = str.toCharArray()
        val openingChars = ArrayDeque<Char>(1)
        for (char in chars) {
            if (char in openingEndingPairs.keys) {
                openingChars.add(char)
            } else {
                val lastOpening = openingChars.removeLast()
                if (openingEndingPairs[lastOpening] != char) {
                    val corruptedValue = getCorruptedValue(char)
                    println("String $str is corrupted with value $corruptedValue")
                    return corruptedValue
                }
            }
        }
        return 0
    }

    private fun getIncompleteValue(str: String): Long {
        val chars = str.toCharArray()
        val openingChars = ArrayDeque<Char>(1)
        for (char in chars) {
            if (char in openingEndingPairs.keys) {
                openingChars.add(char)
            } else {
                openingChars.removeLast()
            }
        }

        println("leftover openings: $openingChars")
        val incompleteValue = calculateIncompleteValue(openingChars)
        println("String $str is incomplete with value $incompleteValue")
        return incompleteValue
    }

    private fun calculateIncompleteValue(leftoverOpenings: ArrayDeque<Char>): Long {
        var total = 0L
        while (leftoverOpenings.isNotEmpty()) {
            total *= 5
            total += getIncompleteValue(leftoverOpenings.removeLast())
        }
        return total
    }

    private fun getCorruptedValue(char: Char): Int {
        return when (char) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> throw IllegalStateException("wrong ending char")
        }
    }

    private fun getIncompleteValue(char: Char): Int {
        return when (char) {
            '(' -> 1
            '[' -> 2
            '{' -> 3
            '<' -> 4
            else -> throw IllegalStateException("wrong opening char $char")
        }
    }

}