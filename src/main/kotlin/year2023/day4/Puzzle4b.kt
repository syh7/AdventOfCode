package year2023.day4

import readFile


fun main() {
    val cards = readFile("year2023/day4/actual.txt")
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


}
