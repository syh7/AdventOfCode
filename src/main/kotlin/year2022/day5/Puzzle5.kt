package year2022.day5

import readFile

fun main() {
    val lines = readFile("year2022/day5/actual.txt")

    val amountOfStacks = (lines[0].length + 1) / 4

    val containerIndex = lines.indexOfFirst { it.contains("1") }

    val dequeMap = mutableMapOf<Int, ArrayDeque<String>>()
    for (i in 1..amountOfStacks) {
        dequeMap[i] = ArrayDeque()
    }

    for (lineNr in containerIndex - 1 downTo 0) {
        val chars = lines[lineNr].split("")
        for ((index, char) in chars.withIndex()) {
            if ((index - 2) % 4 == 0 && char != " ") {
                val dequeIndex = ((index - 2) / 4) + 1
                dequeMap[dequeIndex]!!.add(char)
            }
        }
    }

    dequeMap.forEach { (index, deque) -> println("deque $index: $deque") }

    lines.drop(containerIndex + 2).forEach { line ->
        println("handling line $line")
        val (amount, source, destination) = line.split(" ")
            .mapNotNull { it.toIntOrNull() }

        moveB(amount, dequeMap, source, destination)

    }


    dequeMap.forEach { (index, deque) -> println("deque $index: $deque") }
    println("top elements: ${dequeMap.map { (_, deque) -> deque.last() }.joinToString("") { it }}")


}

private fun moveA(amount: Int, dequeMap: MutableMap<Int, ArrayDeque<String>>, source: Int, destination: Int) {
    for (i in 1..amount) {
        val element = dequeMap[source]!!.removeLast()
        dequeMap[destination]!!.add(element)
        println("Moved $element from deque $source to deque $destination")
    }
}

private fun moveB(amount: Int, dequeMap: MutableMap<Int, ArrayDeque<String>>, source: Int, destination: Int) {
    val tempDeque = ArrayDeque<String>()

    for (i in 1..amount) {
        val element = dequeMap[source]!!.removeLast()
        tempDeque.add(element)
        println("Moved $element from deque $source to tempDeque")
    }
    for (i in 1..amount) {
        val element = tempDeque.removeLast()
        dequeMap[destination]!!.add(element)
        println("Moved $element from tempDeque to deque $destination")
    }
}
