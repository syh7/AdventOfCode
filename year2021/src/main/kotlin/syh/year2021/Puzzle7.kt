package syh.year2021

import kotlin.math.abs
import syh.library.AbstractAocDay

class Puzzle7 : AbstractAocDay(2021, 7) {

    override fun doA(file: String): String {
        val numbers = readSingleLineFile(file)[0].split(",").map { it.toInt() }

        val lowestStepPosition = (numbers.min()..numbers.max())
            .minOf { testPosition -> numbers.sumOf { n -> abs(n - testPosition) } }

        return lowestStepPosition.toString()
    }

    override fun doB(file: String): String {
        val numbers = readSingleLineFile(file)[0].split(",").map { it.toInt() }

        val lowestStepPosition = (numbers.min()..numbers.max())
            .minOf { testPosition -> numbers.sumOf { n -> abs(n - testPosition).let { it * (it + 1) / 2 } } }

        return lowestStepPosition.toString()
    }

}