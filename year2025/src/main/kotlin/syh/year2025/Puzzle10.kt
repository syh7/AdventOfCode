package syh.year2025

import syh.library.AbstractAocDay
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

    private fun minimalCounterPresses(manual: Manual): Int {

        val result = findShortestPathByPredicate(
            start = List(manual.expectedValues.size) { 0 },
            endFunction = { checkEqual(manual.joltage, it) },
            neighbours = { initial ->
                manual.buttons.map { button ->
                    initial.mapIndexed { index, n -> if (button.contains(index)) n + 1 else n }
                }
                    .filter { !joltageToHigh(it, manual.joltage) }
            },
            cost = { current, _ -> if (joltageToHigh(current, manual.joltage)) 1000 else 1 }
        )
        println("for manual $manual")
        println("manual has score ${result.getScore()}")
        println("found joltage ${result.getPath()}")
        return result.getScore()
    }

    private fun joltageToHigh(current: List<Int>, expected: List<Int>): Boolean {
        for (i in current.indices) {
            if (current[i] > expected[i]) {
                return true
            }
        }
        return false
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