package syh.year2024.day19

import syh.AbstractAocDay
import syh.library.Memoization


class Puzzle19 : AbstractAocDay(2024, 19) {
    override fun doA(file: String): String {
        val (a, b) = readDoubleLineFile(file)
        val towels = a.split(", ")
        val designs = b.split("\r\n")

        println("available towels: $towels")

        val memoization = Memoization<String, Boolean>()
        memoization.addBaseCase("", true)

        val keyTransformer = { design: String -> towels.filter { towel -> design.startsWith(towel) }.map { towel -> design.removePrefix(towel) } }
        val valueReducer = { x: Boolean, y: Boolean -> x || y }
        val exitCondition = { bool: Boolean -> bool }
        val validDesigns = designs.count { memoization.memoizeEarlyExit(it, false, keyTransformer, valueReducer, exitCondition) }

        return validDesigns.toString()
    }

    override fun doB(file: String): String {
        val (a, b) = readDoubleLineFile(file)
        val towels = a.split(", ")
        val designs = b.split("\r\n")

        println("available towels: $towels")

        val memoization = Memoization<String, Long>()
        memoization.addBaseCase("", 1L)

        val keyTransformer = { design: String -> towels.filter { towel -> design.startsWith(towel) }.map { towel -> design.removePrefix(towel) } }
        val valueReducer = { x: Long, y: Long -> x + y }
        val validTowelOrders = designs.sumOf { memoization.memoize(it, 0L, keyTransformer, valueReducer) }

        return validTowelOrders.toString()
    }
}