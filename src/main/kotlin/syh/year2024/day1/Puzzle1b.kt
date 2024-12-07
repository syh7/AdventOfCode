package syh.year2024.day1

import syh.readSingleLineFile

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

    val similarityList = mutableListOf<Int>()
    for (i in 0 until leftList.size) {
        val occurrences = rightList.count { it == leftList[i] }
        similarityList.add(occurrences * leftList[i])
    }

    val totalDifferences = similarityList.sum()
    println("total: $totalDifferences")
}