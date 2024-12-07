package syh.year2023.day2

data class Game(
    val id: Int,
    val sets: List<CubeSet>
)

data class CubeSet(
    var reds: Int = 0,
    var greens: Int = 0,
    var blues: Int = 0,
)

fun readGame(str: String): Game {
    val (gameId, unconvertedSets) = str.split(": ")

    val id = gameId.split(" ")[1].toInt()
    val sets = unconvertedSets.split("; ")
        .map { set ->
            val cubeSet = CubeSet()
            set.split(", ").forEach { str ->
                val (amount, colour) = str.split(" ")
                if (colour == "red") {
                    cubeSet.reds = amount.toInt()
                }
                if (colour == "blue") {
                    cubeSet.blues = amount.toInt()
                }
                if (colour == "green") {
                    cubeSet.greens = amount.toInt()
                }
            }
            cubeSet
        }
    return Game(id, sets)
}