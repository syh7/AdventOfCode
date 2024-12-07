package syh.year2023.day6

import syh.AbstractAocDay


class Puzzle6 : AbstractAocDay(2023, 6) {
    override fun doA(file: String): Long {
        val lines = readSingleLineFile(file)
        val records = readRecordForA(lines)

        records.forEach { println(it) }

        val total = records.map { calculateDifferentWaysToWin(it) }
            .reduce { acc, element -> element * acc }

        println("total: $total")
        return total
    }

    override fun doB(file: String): Long {
        val lines = readSingleLineFile(file)
        val records = readRecordForB(lines)

        records.forEach { println(it) }

        val total = records.map { calculateDifferentWaysToWin(it) }
            .reduce { acc, element -> element * acc }

        println("total: $total")
        return total
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
        val filteredLines = lines
            .map { it.split(": ")[1] }
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
            val boatTime = timeDistanceRecord.time - i

            val totalDistance = i * boatTime

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

    data class TimeDistanceRecord(val time: Long, val distanceToBeat: Long)
}

