package syh.year2021

import syh.library.AbstractAocDay

class Puzzle1 : AbstractAocDay(2021, 1) {
    override fun doA(file: String): String {
        return readSingleLineFile(file)
            .map { it.toInt() }
            .zipWithNext { a, b ->
                val comparison = a < b
//                println("comparing $a and $b results in $comparison")
                comparison
            }
            .count { it }
            .toString()
    }

    override fun doB(file: String): String {
        val numbers = readSingleLineFile(file).map { it.toInt() }
        val groups = (2..<numbers.size).map { index ->
            val total = numbers[index - 2] + numbers[index - 1] + numbers[index]
            println("group $index has total $total")
            total
        }
        return groups
            .zipWithNext { a, b ->
                val comparison = a < b
//                println("comparing $a and $b results in $comparison")
                comparison
            }
            .count { it }
            .toString()
    }

}