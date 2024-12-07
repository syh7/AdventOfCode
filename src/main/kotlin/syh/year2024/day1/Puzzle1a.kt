package syh.year2024.day1

import syh.readSingleLineFile
import kotlin.math.absoluteValue

fun main() {
    val leftList = mutableListOf<Int>()
    val rightList = mutableListOf<Int>()

    readSingleLineFile("year2024/day1/actual.txt")
        .forEach {
            val (left, right) = it.split("   ")
            leftList.add(left.toInt())
            rightList.add(right.toInt())
        }
    leftList.sort()
    rightList.sort()

    val differences = mutableListOf<Int>()
    for (i in 0 until leftList.size) {
        differences.add(leftList[i] - rightList[i])
    }
    val totalDifferences = differences.sumOf { it.absoluteValue }
    println("total: $totalDifferences")
}