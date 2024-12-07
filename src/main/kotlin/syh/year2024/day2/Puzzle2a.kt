package syh.year2024.day2

import syh.readSingleLineFile
import kotlin.math.absoluteValue

fun main() {
    val reports = mutableListOf<List<Int>>()

    readSingleLineFile("year2024/day2/actual.txt")
        .forEach {
            val split = it.split(" ")
            val list = split.map { n -> n.toInt() }
            reports.add(list)
        }



    println("total: ${reports.count { isSafe(it) }}")
}

private fun isSafe(list: List<Int>): Boolean {
    val expectedParity = findParity(list[0], list[1])

    for (i in 0 until list.size - 1) {
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

private fun findParity(a: Int, b: Int): Parity {
    return if (a > b) {
        Parity.DECREASING
    } else {
        Parity.INCREASING
    }
}


