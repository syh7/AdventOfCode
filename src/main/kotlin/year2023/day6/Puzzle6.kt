package year2023.day6

import readFile

data class TimeDistanceRecord(val time: Long, val distanceToBeat: Long)

fun main() {
    val lines = readFile("year2023/day6/actual.txt")

    val records = readRecordForB(lines)

    records.forEach { println(it) }

    val total = records.map { calculateDifferentWaysToWin(it) }
        .reduce { acc, element -> element * acc }

    println("total: $total")

}

private fun readRecordForA(lines: List<String>): List<TimeDistanceRecord> {
    val filteredLines = lines.map { it.split(": ")[1] }
        .map { it.split(" ") }
        .map { it.map { itt -> itt.trim() }.filter { itt -> itt.isNotEmpty() }.map { itt -> itt.toLong() } }


    val records = mutableListOf<TimeDistanceRecord>()
    for (i in filteredLines[0].indices) {
        records.add(TimeDistanceRecord(filteredLines[0][i], filteredLines[1][i]))
    }
    return records
}

private fun readRecordForB(lines: List<String>): List<TimeDistanceRecord> {
    val filteredLines = lines.map { it.split(": ")[1] }
        .map { it.split(" ") }
        .map { it.filter { itt -> itt.isNotEmpty() } }
        .map { line -> line.joinToString("") { it }.takeWhile { it.isDigit() } }
        .map { it.toLong() }
    filteredLines.forEach { println("line: '$it'") }

    val timeDistanceRecord = TimeDistanceRecord(filteredLines[0], filteredLines[1])

    return listOf(timeDistanceRecord)
}

private fun calculateDifferentWaysToWin(timeDistanceRecord: TimeDistanceRecord): Long {
    println("Calculating ways to win for record: $timeDistanceRecord")
    var waysToWin = 0L

    for (i in 1..timeDistanceRecord.time) {
        val loadTime = i
        val boatTime = timeDistanceRecord.time - i

        val totalDistance = loadTime * boatTime

        if (totalDistance > timeDistanceRecord.distanceToBeat) {
            waysToWin++

            if (waysToWin % 10000 == 0L) {
                println("loadTime: $i and boatTime: $boatTime and totalDistance: $totalDistance")
            }
        }

    }
    println("Found $waysToWin ways to win")
    println()
    return waysToWin
}

