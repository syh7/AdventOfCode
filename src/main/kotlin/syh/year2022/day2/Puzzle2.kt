package syh.year2022.day2

import syh.AbstractAocDay

class Puzzle2 : AbstractAocDay(2022, 2) {
    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)
        return lines.sumOf { calculateScoreForA(it) }.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)
        return lines.sumOf { calculateScoreForB(it) }.toString()
    }

    private fun calculateScoreForA(line: String): Int {
        val (opponent, own) = line.split(" ")
        val win = 6
        val draw = 3
        val loss = 0

        // ROCK     1 A X
        // PAPER    2 B Y
        // SCISSORS 3 C Z
        if (opponent == "A") {
            if (own == "X") return 1 + draw
            if (own == "Y") return 2 + win
            if (own == "Z") return 3 + loss
        } else if (opponent == "B") {
            if (own == "X") return 1 + loss
            if (own == "Y") return 2 + draw
            if (own == "Z") return 3 + win
        } else if (opponent == "C") {
            if (own == "X") return 1 + win
            if (own == "Y") return 2 + loss
            if (own == "Z") return 3 + draw
        }
        throw IllegalStateException("invalid line: $line")
    }

    private fun calculateScoreForB(line: String): Int {
        val (opponent, own) = line.split(" ")
        val win = 6
        val draw = 3
        val loss = 0

        // ROCK     1 A
        // PAPER    2 B
        // SCISSORS 3 C
        // X lose
        // Y draw
        // Z win
        if (opponent == "A") { // rock
            if (own == "X") return 3 + loss
            if (own == "Y") return 1 + draw
            if (own == "Z") return 2 + win
        } else if (opponent == "B") { // paper
            if (own == "X") return 1 + loss
            if (own == "Y") return 2 + draw
            if (own == "Z") return 3 + win
        } else if (opponent == "C") { // scissors
            if (own == "X") return 2 + loss
            if (own == "Y") return 3 + draw
            if (own == "Z") return 1 + win
        }
        throw IllegalStateException("invalid line: $line")
    }
}