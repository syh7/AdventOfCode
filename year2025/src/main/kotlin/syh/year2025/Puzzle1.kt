package syh.year2025

import kotlin.math.abs
import syh.library.AbstractAocDay

class Puzzle1 : AbstractAocDay(2025, 1) {

    override fun doA(file: String): String {

        val totalExactZeroes = readSingleLineFile(file)
            .map { it.replace("L", "-") }
            .map { it.replace("R", "+") }
            .map { it.toInt() }
            .fold(50 to 0) { (previousDial, previousZeroes), nextDial ->
                val newDial = (previousDial + nextDial).mod(100)
                val newZero = if (newDial == 0) 1 else 0
                newDial to previousZeroes + newZero
            }
            .second

        return totalExactZeroes.toString()
    }

    override fun doB(file: String): String {

        println("Starting with 50 to 0")
        val totalPassedZeroes = readSingleLineFile(file)
            .map { it.replace("L", "-") }
            .map { it.replace("R", "+") }
            .map { it.toInt() }
            .fold(50 to 0) { (previousDial, previousZeroes), nextDial ->
                val newDial = (previousDial + nextDial).mod(100)

                var rotations = abs((previousDial + nextDial) / 100)
                if (nextDial < 0 && previousDial != 0 && (previousDial + nextDial) <= 0) rotations++

                val newZeroes = previousZeroes + rotations
                println("Calculating $nextDial results in $newDial to $newZeroes")
                newDial to newZeroes
            }
            .second

        return totalPassedZeroes.toString()
    }

}