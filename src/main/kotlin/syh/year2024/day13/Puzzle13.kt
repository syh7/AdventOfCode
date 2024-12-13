package syh.year2024.day13

import syh.AbstractAocDay
import kotlin.math.floor


class Puzzle13 : AbstractAocDay(2024, 13) {
    override fun doA(file: String): String {
        val machines = readDoubleLineFile(file)
            .map { lines -> Machine.readMachine(lines) }
        machines.forEach { println(it) }

        val sum = machines.sumOf {
            println("calculating steps for machine $it")
            val steps = minimumSteps(it)
            println("$it took $steps steps")
            steps
        }
        println("total minimum steps $sum")
        return sum.toString()
    }

    override fun doB(file: String): String {
        val machines = readDoubleLineFile(file)
            .map { lines -> Machine.readMachine(lines, 10000000000000) }
        machines.forEach { println(it) }

        val sum = machines.sumOf {
            println("calculating steps for machine $it")
            val steps = minimumSteps(it)
            println("$it took $steps steps")
            steps
        }
        println("total minimum steps $sum")
        return sum.toString()
    }

    private fun minimumSteps(machine: Machine): Long {
        // aX*a+bX*b = pX
        // aY*a+bY*b = pY
        // thus
        // a = ( pX*bY - pY*bX ) / ( aX*bY - aY*bX )
        // b = ( pY*aX - pX*aY ) / ( aX*bY - aY*bX )

        val divisor = machine.buttonA.x * machine.buttonB.y - machine.buttonA.y * machine.buttonB.x
        val a: Double = (machine.prize.x * machine.buttonB.y - machine.prize.y * machine.buttonB.x).toDouble() / divisor
        val b: Double = (machine.prize.y * machine.buttonA.x - machine.prize.x * machine.buttonA.y).toDouble() / divisor
        println("a: $a")
        println("b: $b")
        println("d: $divisor")

        // we're only interested in Int results, so full, rounded numbers
        // check if the floor of a number is the same as the number, then it is round
        if (floor(a) == a && floor(b) == b) {
            return (3 * a + b).toLong()
        }
        return 0
    }

    data class Coord(val x: Long, val y: Long) {
        override fun toString(): String {
            return "[$x][$y]"
        }
    }

    data class Machine(val buttonA: Coord, val buttonB: Coord, val prize: Coord) {
        companion object {
            fun readMachine(lines: String, add: Long = 0): Machine {
                val split = lines.split("\r\n")
                val (aX, aY) = split[0].split(", ").map { it.split("+")[1] }.map { c -> c.toLong() }
                val (bX, bY) = split[1].split(", ").map { it.split("+")[1] }.map { c -> c.toLong() }
                val (prizeX, prizeY) = split[2].split(", ").map { it.split("=")[1] }.map { c -> c.toLong() + add }
                return Machine(Coord(aX, aY), Coord(bX, bY), Coord(prizeX, prizeY))
            }
        }
    }

}