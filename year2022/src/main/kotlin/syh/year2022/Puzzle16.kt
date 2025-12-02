package syh.year2022

import kotlin.math.max
import kotlin.math.min
import syh.library.AbstractAocDay


class Puzzle16 : AbstractAocDay(2022, 16) {

    override fun doA(file: String): String {
        val valves = readSingleLineFile(file).map { Valve.parse(it) }
        valves.forEach { println(it) }

        val distances = floydWarshall(valves)
        val startValve = valves.first { it.id == "AA" }
        val relevantValves = valves.filter { it.flow > 0 }

        val possibleStates = visit(startValve, 30, 0, relevantValves, mutableSetOf(), distances, mutableMapOf())

//        println("possible states:")
//        possibleStates.forEach { println(it) }

        val max = possibleStates.values.max()
        println("max: $max")
        return max.toString()
    }

    override fun doB(file: String): String {
        val valves = readSingleLineFile(file).map { Valve.parse(it) }
        valves.forEach { println(it) }

        val distances = floydWarshall(valves)
        val startValve = valves.first { it.id == "AA" }
        val relevantValves = valves.filter { it.flow > 0 }

        val possibleStates = visit(startValve, 26, 0, relevantValves, mutableSetOf(), distances, mutableMapOf())

//        println("possible states:")
//        possibleStates.forEach { (key, value) -> println("${key.map { it.id }}=$value") }

        // we want the maximum from two states where the valves opened are unique
        // each valve can only count for either the elephant or us, not for both
        // so find the two sets with unique valves that have the highest combined value
        val max = possibleStates.maxOf { (firstKey, firstValue) ->

            val maxSecondValue = possibleStates.keys
                .filter { secondKey -> secondKey.none { it in firstKey } }
                .maxOfOrNull { possibleStates[it]!! }
            val totalValue = firstValue + (maxSecondValue ?: 0)

            totalValue
        }

        println("max: $max")
        return max.toString()
    }

    private fun visit(currentValve: Valve, minutesLeft: Int, currentFlow: Int, allValves: List<Valve>, openedValves: Set<Valve>, distances: MutableMap<Valve, MutableMap<Valve, Int>>, answer: MutableMap<Set<Valve>, Int>): MutableMap<Set<Valve>, Int> {
//        println("visiting valve ${currentValve.id} with open valves: ${openedValves.map { it.id }} and minutes left $minutesLeft")
        answer[openedValves] = max(answer[openedValves] ?: 0, currentFlow)

        allValves.filter { it !in openedValves }
            .forEach { valve ->
                val newMinutes = minutesLeft - distances[currentValve]!![valve]!! - 1
                if (newMinutes > 0) {
                    val newOpenedValves = openedValves.toMutableSet()
                    newOpenedValves.add(valve)
                    visit(valve, newMinutes, currentFlow + newMinutes * valve.flow, allValves, newOpenedValves, distances, answer)
                }
            }
        return answer
    }

    private fun floydWarshall(valves: List<Valve>): MutableMap<Valve, MutableMap<Valve, Int>> {
        // create distances map of valve to valve with value of shortest path

        // init distances to neighbours and itself
        val distances = mutableMapOf<Valve, MutableMap<Valve, Int>>()
        for (valve in valves) {
            val valveMap = mutableMapOf<Valve, Int>()

            for (otherValve in valves) {
                valveMap[otherValve] = 100
            }

            valveMap[valve] = 0
            valves.filter { it.id in valve.tunnels }
                .forEach { valveMap[it] = 1 }

            distances[valve] = valveMap
        }

        // compute the shortest path between i and j with k steps
        // where k is 0..N of valves size
        // this gives the shortest path between i and j with all possible intermediate vertices
        for (k in valves.indices) {
            val valveK = valves[k]
            for (i in valves.indices) {
                val valveI = valves[i]
                for (j in valves.indices) {
                    val valveJ = valves[j]
                    distances[valveI]!![valveJ] = min(distances[valveI]!![valveJ]!!, distances[valveI]!![valveK]!! + distances[valveK]!![valveJ]!!)
                }
            }
        }

        println("created floyd wayshall distance map:")
        distances.forEach { println(it) }

        return distances
    }

    data class Valve(val id: String, val flow: Int, val tunnels: Set<String>, var opened: Boolean = false) {
        companion object {
            fun parse(str: String): Valve {
                val (name, rest) = str.removePrefix("Valve ").split(" ", limit = 2)
                val (flow, tunnelRest) = rest.removePrefix("has flow rate=").split("; ", limit = 2)
                val tunnels = tunnelRest
                    .removePrefix("tunnels lead to valves ")
                    .removePrefix("tunnel leads to valve ")
                    .split(", ").toSet()

                return Valve(name, flow.toInt(), tunnels)
            }
        }
    }
}
