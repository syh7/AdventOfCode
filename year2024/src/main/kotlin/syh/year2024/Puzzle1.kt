package syh.year2024

import kotlin.math.absoluteValue
import syh.library.AbstractAocDay

class Puzzle1 : AbstractAocDay(2024, 1) {
    override fun doA(file: String): String {
        val (leftList, rightList) = readLeftAndRightList(file)

        val differences = mutableListOf<Int>()
        for (i in leftList.indices) {
            differences.add(leftList[i] - rightList[i])
        }
        val totalDifferences = differences.sumOf { it.absoluteValue }
        println("total: $totalDifferences")
        return totalDifferences.toString()
    }

    override fun doB(file: String): String {
        val (leftList, rightList) = readLeftAndRightList(file)

        val similarityList = mutableListOf<Int>()
        for (i in leftList.indices) {
            val occurrences = rightList.count { it == leftList[i] }
            similarityList.add(occurrences * leftList[i])
        }

        val totalDifferences = similarityList.sum()
        println("total: $totalDifferences")
        return totalDifferences.toString()
    }

    private fun readLeftAndRightList(file: String): Pair<MutableList<Int>, MutableList<Int>> {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()

        readSingleLineFile(file)
            .forEach {
                val (left, right) = it.split("   ")
                leftList.add(left.toInt())
                rightList.add(right.toInt())
            }
        leftList.sort()
        rightList.sort()
        return Pair(leftList, rightList)
    }

}