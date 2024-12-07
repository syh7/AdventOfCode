package syh.year2022.day6

import syh.readSingleLineFile


fun main() {
    val line = readSingleLineFile("year2022/day6/actual.txt")[0]
    val markerLength = 14 // change to 4 for 6A

    val chars = line.split("")

    var index = 0
    val knownChars = mutableSetOf<String>()
    while (knownChars.size < markerLength) {
        index++
        knownChars.add(chars[index])
    }

    println("Found 4 different characters $knownChars at index $index")

    val markerIndex: Int
    while (true) {
        val subList = chars.subList(index - markerLength + 1, index + 1)
        if (subList.toSet().size == markerLength) {
            markerIndex = index
            break
        }
        index++
    }

    println("marker found at $markerIndex")
}
