package syh.year2023

import syh.library.AbstractAocDay

class Puzzle9 : AbstractAocDay(2023, 9) {
    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)
            .map { it.split(" ") }
            .map { it.map { i -> i.toLong() } }

        val allHistories = lines.map { mutableListOf(it) + calculateHistories(it) }

        val extrapolateFuture = extrapolateFutureSum(allHistories)
        println("extrapolated future: $extrapolateFuture")

        return extrapolateFuture.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)
            .map { it.split(" ") }
            .map { it.map { i -> i.toLong() } }

        val allHistories = lines.map { mutableListOf(it) + calculateHistories(it) }

        val extrapolateHistoryWithReduce = extrapolateHistorySumWithReduce(allHistories)
        println("extrapolated history: $extrapolateHistoryWithReduce")
        return extrapolateHistoryWithReduce.toString()
    }

    private fun extrapolateFutureSum(allHistories: List<List<List<Long>>>): Long {
        return allHistories.sumOf { histories -> histories.sumOf { history -> history.last() } }
    }

    private fun extrapolateHistorySumWithReduce(allHistories: List<List<List<Long>>>): Long {
        return allHistories.sumOf { histories ->
            histories.reversed()
                .map { it.first() }
                .reduce { acc, first -> first - acc }
        }
    }

    private fun calculateHistories(values: List<Long>): MutableList<MutableList<Long>> {
        val history = mutableListOf<Long>()

        for (i in 1..<values.size) {
            history.add(values[i] - values[i - 1])
        }

        if (history.all { it == 0L }) {
            return mutableListOf(history)
        }

        val histories = calculateHistories(history)
        histories.add(0, history)
        return histories
    }
}