package syh.year2025

import syh.library.AbstractAocDay
import syh.library.GaussianSolver
import syh.library.findShortestPathByPredicate

class Puzzle10 : AbstractAocDay(2025, 10) {
    override fun doA(file: String): String {
        return readSingleLineFile(file)
            .map { Manual.parse(it) }
            .sumOf { minimalAPresses(it) }
            .toString()
    }

    override fun doB(file: String): String {
        return readSingleLineFile(file)
            .map { Manual.parse(it) }
            .sumOf { minimalCounterPresses(it) }
            .toString()
    }

    private fun minimalAPresses(manual: Manual): Int {

        val result = findShortestPathByPredicate(
            start = List(manual.expectedValues.size) { false },
            endFunction = { checkEqual(manual.expectedValues, it) },
            neighbours = { initial ->
                manual.buttons.map { button ->
                    initial.mapIndexed { index, b -> if (button.contains(index)) !b else b }
                }
            },
        )
        println("for manual $manual")
        println("manual has score ${result.getScore()}")
        println("found buttons ${result.getPath()}")
        return result.getScore()
    }

    private fun minimalCounterPresses(manual: Manual): Long {

        val nrOfCounters = manual.joltage.size
        val nrOfButtons = manual.buttons.size

        val coefficients = Array(nrOfCounters) { IntArray(nrOfButtons) }
        for (b in 0..<nrOfButtons) {
            for (idx in manual.buttons[b]) {
                if (idx < nrOfCounters) {
                    coefficients[idx][b] = 1
                }
            }
        }

        return GaussianSolver.solve(coefficients, manual.joltage.toIntArray())
    }

    private fun <T> checkEqual(a: List<T>, b: List<T>): Boolean {
        if (a.size != b.size) {
            return false
        }

        for (i in a.indices) {
            if (a[i] != b[i]) {
                return false
            }
        }

        return true
    }

    private data class Manual(
        val expectedValues: List<Boolean>,
        val buttons: List<List<Int>>,
        val joltage: List<Int>
    ) {

        companion object {
            fun parse(str: String): Manual {
                val (expectedPattern, leftoverString) = str.removePrefix("[").split("] (")
                val (buttonPattern, joltagePattern) = leftoverString.split(") {")

                val expectedValues = expectedPattern.map { it == '#' }
                val buttons = buttonPattern
                    .split(") (")
                    .map { it.split(",").map { i -> i.toInt() } }
                val joltage = joltagePattern.removeSuffix("}")
                    .split(",")
                    .map { it.toInt() }

                return Manual(expectedValues, buttons, joltage)
            }
        }
    }

}