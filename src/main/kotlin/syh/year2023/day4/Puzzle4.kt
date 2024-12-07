package syh.year2023.day4

import syh.AbstractAocDay
import kotlin.math.pow

class Puzzle4 : AbstractAocDay(2023, 4) {
    override fun doA(file: String): Long {
        val cards = readSingleLineFile(file)
            .map { readCard(it) }
        cards.forEach { println(it) }

        val total = cards.sumOf { card ->
            val matchedNumbers = card.owned.count { card.winners.contains(it) }
            val points: Double = if (matchedNumbers > 0) 2.0.pow(matchedNumbers - 1) else 0.0
            println("Card ${card.id} is worth $points points")
            points
        }
        println("total: $total")
        return total.toLong()
    }

    override fun doB(file: String): Long {
        val cards = readSingleLineFile(file)
            .map { readCard(it) }
        cards.forEach { println(it) }

        val cardMultiplier = cards.associate { card -> card.id to 1 }.toMutableMap()

        cards.forEach { card ->
            val matchedNumbers = card.owned.count { card.winners.contains(it) }
            if (matchedNumbers > 0) {
                for (i in 1..matchedNumbers) {
                    val instances = cardMultiplier[card.id]!!
                    cardMultiplier.compute(card.id + i) { _: Int, value: Int? -> value!! + instances }
                }
            }
        }
        cardMultiplier.forEach { println(it) }
        val total = cardMultiplier.values.sum()

        println("total: $total")
        return total.toLong()
    }

    data class Card(
        val id: Int,
        val owned: List<Int>,
        val winners: List<Int>,
    )

    private fun readCard(str: String): Card {
        val (cardId, allNumbers) = str.split(": ")
        val id = cardId.split(" ").last().toInt()

        val (winNumbers, ownedNumbers) = allNumbers.split(" | ")
        println("winners: '$winNumbers'")
        println("owned: '$ownedNumbers'")
        val winners = winNumbers.split(" ").filter { it != "" }.map { it.toInt() }
        val owned = ownedNumbers.split(" ").filter { it != "" }.map { it.toInt() }

        return Card(id, owned, winners)
    }
}
