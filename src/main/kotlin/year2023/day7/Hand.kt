package year2023.day7

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

fun findType(occurrencesMap: MutableMap<String, Int>): HandType {
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