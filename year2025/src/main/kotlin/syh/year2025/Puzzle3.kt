package syh.year2025

import syh.library.AbstractAocDay

class Puzzle3 : AbstractAocDay(2025, 3) {
    override fun doA(file: String): String {
        return readSingleLineFile(file)
            .sumOf { line ->
                val numbers = line.split("").filter { it.isNotBlank() }.map { it.toInt() }
                getHighestNumber(numbers, 2)
            }
            .toString()
    }

    override fun doB(file: String): String {
        return readSingleLineFile(file)
            .sumOf { line ->
                val numbers = line.split("").filter { it.isNotBlank() }.map { it.toInt() }
                getHighestNumber(numbers, 12)
            }
            .toString()
    }

    private fun getHighestNumber(initial: List<Int>, totalSize: Int): Long {
        var totalNumber = ""
        var numbers = initial
        for (leftoverRequirement in totalSize downTo 1) {
            val index = getHighestValidIndex(numbers, leftoverRequirement)
            totalNumber += numbers[index]
            numbers = numbers.drop(index + 1)

        }
        println(initial)
        println(totalNumber)
        return totalNumber.toLong()

    }

    private fun getHighestValidIndex(numbers: List<Int>, leftoverRequirement: Int): Int {
        for (i in 9 downTo 1) {
            val index = numbers.indexOf(i)
            if (index == -1 || index > numbers.size - leftoverRequirement) {
                continue
            }
            return index
        }
        throw IllegalStateException("leftoverRequirement: $leftoverRequirement but numbers is ${numbers.size}")
    }
}