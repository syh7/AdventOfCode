package syh.year2025

import syh.library.AbstractAocDay

class Puzzle3 : AbstractAocDay(2025, 3) {
    override fun doA(file: String): String {
        return readSingleLineFile(file)
            .sumOf { line ->
                val numbers = line.split("").filter { it.isNotBlank() }.map { it.toInt() }
                println(numbers)

                var highest = 0
                var secondHighest = 0

                for (i in 9 downTo 1) {
                    val index = numbers.indexOf(i)
                    if (index == -1 || index == numbers.size - 1) {
                        continue
                    }
                    highest = i
                    secondHighest = numbers.drop(index + 1).max()
                    break
                }
                val str = "$highest$secondHighest"
                println(str)
                str.toInt()
            }
            .toString()
    }

    override fun doB(file: String): String {
        return readSingleLineFile(file)
            .sumOf { line ->
                var numbers = line.split("").filter { it.isNotBlank() }.map { it.toInt() }
                println(numbers)

                var total = ""

                for (leftoverRequirement in 12 downTo 1) {
                    val index = getHighestValidIndex(numbers, leftoverRequirement)
                    total += numbers[index]
                    numbers = numbers.drop(index + 1)

                }
                println(total)
                total.toLong()
            }
            .toString()
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