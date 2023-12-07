package year2023.day7

import readFile


private const val ORDER_LIST = "AKQJT98765432"

fun main() {
    val hands = readFile("year2023/day7/actual.txt").map {
        val (cards, bid) = it.split(" ")
        Hand(cards, bid.toInt(), getType(cards))
    }.toMutableList()

    hands.forEach { println(it) }

    hands.sortWith { a, b -> a.compareTo(b, ORDER_LIST) }

    val total = hands.mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
    println("total: $total")
}

private fun getType(cards: String): HandType {
    if (cards.toSet().size == 1) {
        return HandType.FIVE_KIND
    }
    val occurrencesMap = mutableMapOf<String, Int>()
    for (c in cards) {
        occurrencesMap.putIfAbsent(c.toString(), 0)
        occurrencesMap[c.toString()] = occurrencesMap[c.toString()]!! + 1
    }

    return findType(occurrencesMap)
}