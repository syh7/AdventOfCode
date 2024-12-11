package syh.year2024.day2

import syh.AbstractAocDay
import kotlin.math.absoluteValue

class Puzzle2 : AbstractAocDay(2024, 2) {
    override fun doA(file: String): String {
        val reports = readSingleLineFile(file)
            .map {
                it.split(" ").map { n -> n.toInt() }
            }
        val safeReports = reports.count { isSafe(it) }
        println("total: $safeReports")
        return safeReports.toString()
    }

    override fun doB(file: String): String {
        val reports = readSingleLineFile(file)
            .map {
                it.split(" ").map { n -> n.toInt() }
            }
        val safeReports = reports.count { isRecursiveSafe(it) }
        println("total: $safeReports")
        return safeReports.toString()
    }

    private fun isSafe(list: List<Int>): Boolean {
        val expectedParity = findParity(list[0], list[1])

        for (i in 0..<list.size - 1) {
            val firstNum = list[i]
            val secondNum = list[i + 1]
            if ((firstNum - secondNum).absoluteValue > 3 || firstNum == secondNum) {
                return false
            }
            val currentParity = findParity(firstNum, secondNum)
            if (expectedParity != currentParity) {
                return false
            }
        }
        return true
    }

    private fun isRecursiveSafe(list: List<Int>): Boolean {
        if (isSafe(list)) {
            return true
        }

        for (i in list.indices) {
            val newList = mutableListOf<Int>()
            newList.addAll(list)
            newList.removeAt(i)
            if (isSafe(newList)) {
                return true
            }
        }

        return false
    }

    private fun findParity(a: Int, b: Int): Parity {
        return if (a > b) {
            Parity.DECREASING
        } else {
            Parity.INCREASING
        }
    }

    enum class Parity { INCREASING, DECREASING }
}
