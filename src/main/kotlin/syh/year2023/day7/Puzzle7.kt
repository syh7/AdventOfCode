package syh.year2023.day7

import syh.AbstractAocDay


class Puzzle7 : AbstractAocDay(2023, 7) {
    override fun doA(file: String): String {
        val hands = readSingleLineFile(file).map {
            val (cards, bid) = it.split(" ")
            Hand(cards, bid.toInt(), getType(cards))
        }.toMutableList()

        hands.forEach { println(it) }

        hands.sortWith { a, b -> a.compareTo(b, "AKQJT98765432") }

        val total = hands.mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
        println("total: $total")
        return total.toString()
    }

    override fun doB(file: String): String {
        val hands = readSingleLineFile(file).map {
            val (cards, bid) = it.split(" ")
            Hand(cards, bid.toInt(), getTypeWithJoker(cards))
        }.toMutableList()

        hands.forEach { println(it) }

        hands.sortWith { a, b -> a.compareTo(b, "AKQT98765432J") }

        val total = hands.mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
        println("total: $total")
        return total.toString()
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

    private fun findType(occurrencesMap: MutableMap<String, Int>): HandType {
        if (occurrencesMap.filterValues { it == 2 }.count() == 2) {
            return HandType.TWO_PAIR
        }

        if (occurrencesMap.containsValue(5)) {
            return HandType.FIVE_KIND
        }
        if (occurrencesMap.containsValue(4)) {
            return HandType.FOUR_KIND
        }
        if (occurrencesMap.containsValue(3)) {
            if (occurrencesMap.containsValue(2)) {
                return HandType.FULL_HOUSE
            }
            return HandType.THREE_KIND
        }
        if (occurrencesMap.containsValue(2)) {
            return HandType.ONE_PAIR
        }
        return HandType.HIGH
    }

    private fun getTypeWithJoker(cards: String): HandType {
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
                .minBy { "AKQT98765432J".indexOf(it.key[0]) }
            highestOtherCard.setValue(highestOtherCard.value + jokers)
        }

        return findType(occurrencesMap)
    }

    enum class HandType { FIVE_KIND, FOUR_KIND, FULL_HOUSE, THREE_KIND, TWO_PAIR, ONE_PAIR, HIGH }

    data class Hand(val cards: String, val bid: Int, val type: HandType) {
        fun compareTo(other: Hand, orderList: String): Int {
            return if (this.type == other.type) {
                var result = 0

                for (i in this.cards.indices) {
                    if (this.cards[i] == other.cards[i]) {
                        continue
                    }

                    result = orderList.indexOf(other.cards[i]).compareTo(orderList.indexOf(this.cards[i]))
                    break
                }
                result
            } else {
                other.type.compareTo(this.type)
            }
        }
    }

}