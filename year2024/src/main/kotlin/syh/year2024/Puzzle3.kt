package syh.year2024

import syh.library.AbstractAocDay

class Puzzle3 : AbstractAocDay(2024, 3) {
    override fun doA(file: String): String {
        val regex = Regex("mul\\(\\d{1,3},\\d{1,3}\\)")
        val memory = readSingleLineFile(file).joinToString()

        val matches = regex.findAll(memory)
        val total = matches.map { it.value }
            .map { str ->
                val (firstStr, secondStr) = str.split(",")
                val a = firstStr.split("(")[1].toInt()
                val b = secondStr.split(")")[0].toInt()
                a * b
            }
            .sum()

        return total.toString()
    }

    override fun doB(file: String): String {
        val regex = Regex("mul\\(\\d{1,3},\\d{1,3}\\)|don't\\(\\)|do\\(\\)")
        val memory = readSingleLineFile(file).joinToString()

        val matches = regex.findAll(memory)
        var active = true
        var total = 0
        for (match in matches) {
            if (match.value == "don't()") {
                active = false
            } else if (match.value == "do()") {
                active = true
            } else if (match.value.contains("mul(") && active) {
                val (firstStr, secondStr) = match.value.split(",")
                val a = firstStr.split("(")[1].toInt()
                val b = secondStr.split(")")[0].toInt()
                total += a * b
            }
        }

        return total.toString()
    }
}