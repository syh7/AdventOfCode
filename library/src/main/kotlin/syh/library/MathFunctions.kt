package syh.library

import kotlin.math.max

fun calculateLCM(numbers: List<Long>): Long {
    var lcm = numbers[0]
    for (i in 1..<numbers.size) {
        lcm = calculateLCM(lcm, numbers[i])
    }
    return lcm
}

fun calculateLCM(a: Long, b: Long): Long {
    val largest = if (a > b) a else b
    var lcm = largest
    while (true) {
        if (lcm % a == 0L && lcm % b == 0L) {
            break
        }
        lcm += largest
    }
    return lcm
}

fun List<LongRange>.merge(): List<LongRange> {
    if (this.isEmpty()) return emptyList()

    val sortedRanges = this.sortedBy { it.first }

    val merged = mutableListOf<LongRange>()
    var currentMerge = sortedRanges[0]

    for (range in sortedRanges.drop(1)) {
        if (range.first <= currentMerge.last + 1) {
            currentMerge = currentMerge.first..maxOf(currentMerge.last, range.last)
            println("new merge $currentMerge")
        } else {
            merged.add(currentMerge)
            currentMerge = range
        }
    }
    merged.add(currentMerge)
    return merged
}

private fun mergeRanges(ranges: List<LongRange>): MutableList<LongRange> {
    // only use ranges where first is before last
    val sorted = ranges.sortedBy { it.first }.filter { it.first <= it.last }
    val newRanges = mutableListOf(sorted[0])
    for (currentRange in sorted) {
        val lastNew = newRanges.last()
        if (currentRange.first <= lastNew.last) {
            val newRange = LongRange(lastNew.first, max(currentRange.last, lastNew.last))
            println("current range $currentRange fits in $lastNew so merging to $newRange")
            newRanges.removeLast()
            newRanges.add(newRange)
        } else {
            newRanges.add(currentRange)
            println("could not merge $currentRange")
        }
    }
    return newRanges
}