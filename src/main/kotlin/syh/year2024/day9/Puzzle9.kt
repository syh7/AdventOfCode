package syh.year2024.day9

import syh.AbstractAocDay


class Puzzle9 : AbstractAocDay(2024, 9) {
    override fun doA(file: String): String {
        val chars = readSingleLineFile(file)
            .map { it.split("").filter { c -> c.isNotEmpty() }.map { c -> c.toInt() } }[0]

        println(chars)

        val expandedStrings = expandCharsToLists(chars)

        println(expandedStrings)

        val charMap = expandedStrings.flatten().toMutableList()

        var startIndex = 0
        var lastNumberIndex = charMap.size - 1

        while (startIndex <= lastNumberIndex) {
            if (charMap[startIndex] == ".") {
                while (charMap[lastNumberIndex] == ".") {
                    lastNumberIndex -= 1
                }
                if (lastNumberIndex < startIndex) {
                    break
                }
                charMap[startIndex] = charMap[lastNumberIndex]
                charMap[lastNumberIndex] = "."
            }
            startIndex += 1
        }

        println(charMap)

        return calculateChecksum(charMap).toString()
    }

    override fun doB(file: String): String {
        val chars = readSingleLineFile(file)
            .map { it.split("").filter { c -> c.isNotEmpty() }.map { c -> c.toInt() } }[0]

        println(chars)

        val expandedStrings = expandCharsToLists(chars)

        println(expandedStrings)

        var index = expandedStrings.size - 1
        while (index != 0) {
            if (expandedStrings[index].contains(".")) {
                index--
                continue
            }

            println("trying to move index $index: " + expandedStrings[index])
            val size = expandedStrings[index].size
            val swapIndex = expandedStrings.indexOfFirst { it.count { c -> c == "." } >= size }
            if (swapIndex == -1 || swapIndex >= index) {
                println("could not swap $index")
            } else {
                val startSwapIndex = expandedStrings[swapIndex].indexOfFirst { c -> c == "." }
                for (i in startSwapIndex..<startSwapIndex + size) {
                    expandedStrings[swapIndex][i] = expandedStrings[index][0]
                }
                expandedStrings[index] = MutableList(size) { "." }
            }
            index--
        }

        println(expandedStrings)

        val charMap = expandedStrings.flatten().toMutableList()

        return calculateChecksum(charMap).toString()
    }

    private fun expandCharsToLists(chars: List<Int>): MutableList<MutableList<String>> {
        var counter = 0
        val expandedStrings = chars
            .asSequence()
            .mapIndexed { index, c ->
                if (index % 2 == 1) {
                    MutableList(c) { "." }
                } else {
                    val s = MutableList(c) { "$counter" }
                    counter++
                    s
                }
            }.toMutableList()
        return expandedStrings
    }

    private fun calculateChecksum(charMap: MutableList<String>): Long {
        var total = 0L
        var counter = -1
        for (s in charMap) {
            counter++
            if (s == ".") continue
            total += s.toInt() * counter
        }
        return total
    }
}