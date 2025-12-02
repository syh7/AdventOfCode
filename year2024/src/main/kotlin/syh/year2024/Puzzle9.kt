package syh.year2024

import syh.library.AbstractAocDay

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
        val filesystem = readSingleLineFile(file).map { it.split("").filter { c -> c.isNotEmpty() }.map { c -> c.toInt() } }[0].toMutableList()
        var checksum = 0L

        val openStartIndex = mutableListOf<Int>()
        openStartIndex.add(0)
        for (i in 1..<filesystem.size) {
            openStartIndex.add(openStartIndex[i - 1] + filesystem[i - 1])
        }

        var right = filesystem.size - 1
        while (right >= 0) {
            var found = false
            var left = 1
            while (left < right) {
                if (filesystem[left] >= filesystem[right]) {
                    for (i in 0..<filesystem[right]) {
                        checksum += (right / 2).toLong() * (openStartIndex[left] + i)
                    }
                    filesystem[left] -= filesystem[right]
                    openStartIndex[left] += filesystem[right]
                    found = true
                    break
                }
                left += 2
            }
            if (!found) {
                for (i in 0..<filesystem[right]) {
                    checksum += (right / 2).toLong() * (openStartIndex[right] + i)
                }
            }
            right -= 2
        }

        println(checksum)
        return checksum.toString()
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