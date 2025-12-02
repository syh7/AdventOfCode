package syh.year2022

import syh.library.AbstractAocDay


class Puzzle6 : AbstractAocDay(2022, 6) {
    override fun doA(file: String): String {
        val line = readSingleLineFile(file)[0]
        val markerLength = 4

        return findMarkerIndex(line, markerLength)
    }

    override fun doB(file: String): String {
        val line = readSingleLineFile(file)[0]
        val markerLength = 14

        return findMarkerIndex(line, markerLength)
    }

    private fun findMarkerIndex(line: String, length: Int): String {
        val chars = line.split("")

        var index = 0
        val knownChars = mutableSetOf<String>()
        while (knownChars.size < length) {
            index++
            knownChars.add(chars[index])
        }

        println("Found $length different characters $knownChars at index $index")

        val markerIndex: Int
        while (true) {
            val subList = chars.subList(index - length + 1, index + 1)
            if (subList.toSet().size == length) {
                markerIndex = index
                break
            }
            index++
        }

        println("marker found at $markerIndex")
        return markerIndex.toString()
    }

}