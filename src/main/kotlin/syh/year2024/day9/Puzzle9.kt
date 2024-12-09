package syh.year2024.day9

import syh.AbstractAocDay


class Puzzle9 : AbstractAocDay(2024, 9) {
    override fun doA(file: String): Long {
        val chars = readSingleLineFile(file)
            .map { it.split("").filter { c -> c.isNotEmpty() }.map { c -> c.toInt() } }[0]

        println(chars)

        var counter = 0
        val expandedStrings = chars.mapIndexed { index, c ->
            if (index % 2 == 1) {
                List(c) { "." }
            } else {
                val s = List(c) { "$counter" }
                counter++
                s
            }
        }

        println(expandedStrings)

        val charMap = expandedStrings.flatten().toMutableList()

        var startIndex = 0
        var lastNumberIndex = charMap.size - 1

        while (startIndex <= lastNumberIndex) {
//            println("checking index $startIndex")
            if (charMap[startIndex] == ".") {
//                println("startindex $startIndex: " + charMap[startIndex])
                while (charMap[lastNumberIndex] == ".") {
//                    println("lowering last number index $lastNumberIndex: " + charMap[lastNumberIndex])
                    lastNumberIndex -= 1
                }
                if (lastNumberIndex < startIndex) {
                    break
                }
//                println("swapping index $startIndex with index $lastNumberIndex with number ${charMap[lastNumberIndex]}")
                charMap[startIndex] = charMap[lastNumberIndex]
                charMap[lastNumberIndex] = "."
            }
            startIndex += 1
        }

        println(charMap)

        var total = 0L
        counter = 0
        for (s in charMap) {
            if (s == ".") break
            total += s.toInt() * counter
            counter++
        }

        return total
    }

    override fun doB(file: String): Long {
        val chars = readSingleLineFile(file)
            .map { it.split("").filter { c -> c.isNotEmpty() }.map { c -> c.toInt() } }[0]

        println(chars)

        var counter = 0
        val expandedStrings = chars.mapIndexed { index, c ->
            if (index % 2 == 1) {
                List(c) { "." }.toMutableList()
            } else {
                val s = List(c) { "$counter" }.toMutableList()
                counter++
                s
            }
        }.toMutableList()

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
            if (swapIndex == -1) {
                println("could not swap $index")
            } else {
                val startSwapIndex = expandedStrings[swapIndex].indexOfFirst { c -> c == "." }
                for (i in startSwapIndex..<startSwapIndex + size) {
                    expandedStrings[swapIndex][i] = expandedStrings[index][0]
                }
                expandedStrings[index] = List(size) { "." }.toMutableList()
            }
            index--
        }

        println(expandedStrings)

        val charMap = expandedStrings.flatten().toMutableList()

        var total = 0L
        counter = 0
        for (s in charMap) {
            if (s == ".") continue
            total += s.toInt() * counter
            counter++
        }

        return total

    }
}