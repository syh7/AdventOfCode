package syh.year2023.day4

import syh.readSingleLineFile
import kotlin.math.pow

fun main() {
    val cards = readSingleLineFile("year2023/day4/actual.txt")
        .map { readCard(it) }
    cards.forEach { println(it) }

    val total = cards.sumOf { card ->
        val matchedNumbers = card.owned.count { card.winners.contains(it) }
        val points: Double = if (matchedNumbers > 0) 2.0.pow(matchedNumbers - 1) else 0.0
        println("Card ${card.id} is worth $points points")
        points
    }
    println("total: $total")


}
