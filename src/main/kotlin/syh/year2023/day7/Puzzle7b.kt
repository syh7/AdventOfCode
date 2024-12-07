package syh.year2023.day7

import syh.readSingleLineFile


private const val ORDER_LIST = "AKQT98765432J"

fun main() {
    val hands = readSingleLineFile("year2023/day7/actual.txt").map {
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

    if (cards.contains("J")) {
        val jokers = occurrencesMap["J"]!!
        occurrencesMap.remove("J")
        val maxOtherValue = occurrencesMap.values.max()
        val highestOtherCard = occurrencesMap.entries
            .filter { it.value == maxOtherValue }
            .minBy { ORDER_LIST.indexOf(it.key[0]) }
        highestOtherCard.setValue(highestOtherCard.value + jokers)
    }

    return findType(occurrencesMap)
}

