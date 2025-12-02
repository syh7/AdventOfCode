package syh.year2022

import syh.library.AbstractAocDay


class Puzzle10 : AbstractAocDay(2022, 10) {

    private val LIT = "#"
    private val DARK = " " // supposed to be '.' but this is better readable in the terminal

    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)

        val cycleValues = readProgram(lines)

        val cycleStrength = calculateTotalCycleStrength(cycleValues)
        return cycleStrength.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)

        val cycleValues = readProgram(lines)

        return printCRTScreen(cycleValues)
    }

    private fun readProgram(lines: List<String>): MutableList<Int> {
        val cycleValues = mutableListOf<Int>()

        // yes this will add an extra state up front
        // initially this was to add an initial value so cycleValues.last will give an answer
        // somehow this extra state is expected in the answers though, because if I remove it afterwards with .drop(1)
        // the answers are incorrect
        cycleValues.add(1)

        for (line in lines) {
            if (line.contains("noop")) {
                cycleValues.add(cycleValues.last())

            } else {
                val (_, value) = line.split(" ")
                val newValue = cycleValues.last() + value.toInt()
                cycleValues.add(cycleValues.last())
                cycleValues.add(newValue)
            }
        }
        return cycleValues
    }

    private fun calculateTotalCycleStrength(cycleValues: List<Int>): Int {
        var total = 0
        for (i in 20 until cycleValues.size step 40) {
            val temp = i * cycleValues[i - 1]
            println("Calculated value for cycle $i * ${cycleValues[i - 1]}: ${i * cycleValues[i - 1]}")
            total += temp
        }
        println("Total cycle strength: $total")
        return total
    }

    private fun printCRTScreen(cycleValues: List<Int>): String {
        val screen = cycleValues.chunked(40)
            .map { printCRTScreenLine(it) }
            .joinToString("\n") { it }
        println(screen)
        return screen
    }

    private fun printCRTScreenLine(cycleValues: List<Int>): String {
        return cycleValues
            .mapIndexed { index, cycleValue -> if (index in cycleValue - 1..cycleValue + 1) LIT else DARK }
            .joinToString("") { it }
    }
}