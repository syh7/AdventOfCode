package year2023.day9

import readFile


fun main() {
    val lines = readFile("year2023/day9/actual.txt")
        .map { it.split(" ") }
        .map { it.map { i -> i.toLong() } }

    val allHistories = lines.map { mutableListOf(it) + calculateHistories(it) }

    val extrapolateFuture = extrapolateFutureSum(allHistories)
    println("extrapolated future: $extrapolateFuture")

    val extrapolateHistory = extrapolateHistorySum(allHistories)
    println("extrapolated history: $extrapolateHistory")

    val extrapolateHistoryWithReduce = extrapolateHistorySumWithReduce(allHistories)
    println("extrapolated history: $extrapolateHistoryWithReduce")
}

private fun extrapolateFutureSum(allHistories: List<List<List<Long>>>): Long {
    return allHistories.sumOf { histories -> histories.sumOf { history -> history.last } }
}

private fun extrapolateHistorySum(allHistories: List<List<List<Long>>>): Long {
    return allHistories.sumOf { histories ->
        var tempHistory = 0L
        for (i in histories.size - 2 downTo 0) {
            val first = histories[i].first
            val delta = first - tempHistory
            tempHistory = delta
        }
        tempHistory
    }
}

private fun extrapolateHistorySumWithReduce(allHistories: List<List<List<Long>>>): Long {
    return allHistories.sumOf { histories ->
        histories.reversed()
            .map { it.first }
            .reduce { acc, first -> first - acc }
    }
}

private fun calculateHistories(values: List<Long>): MutableList<MutableList<Long>> {
    val history = mutableListOf<Long>()

    for (i in 1 until values.size) {
        history.add(values[i] - values[i - 1])
    }

    if (history.all { it == 0L }) {
        return mutableListOf(history)
    }

    val histories = calculateHistories(history)
    histories.add(0, history)
    return histories
}