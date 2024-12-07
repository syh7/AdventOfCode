package syh.year2023.day4


data class Card(
    val id: Int,
    val owned: List<Int>,
    val winners: List<Int>,
)


fun readCard(str: String): Card {
    val (cardId, allNumbers) = str.split(": ")
    val id = cardId.split(" ").last().toInt()

    val (winNumbers, ownedNumbers) = allNumbers.split(" | ")
    println("winners: '$winNumbers'")
    println("owned: '$ownedNumbers'")
    val winners = winNumbers.split(" ").filter { it != "" }.map { it.toInt() }
    val owned = ownedNumbers.split(" ").filter { it != "" }.map { it.toInt() }

    return Card(id, owned, winners)
}