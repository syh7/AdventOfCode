package syh.year2022

import syh.library.AbstractAocDay
import syh.library.calculateLCM

class Puzzle11 : AbstractAocDay(2022, 11) {
    override fun doA(file: String): String {
        val monkeys = readDoubleLineFile(file).map { parseMonkey(it) }

        for (i in 0..<20) {
            doRound(monkeys, true, 0)
        }

        monkeys.forEach { println("Monkey ${it.id} inspected items ${it.inspectedItemCount} times") }

        val orderedMonkeys = monkeys.sortedByDescending { it.inspectedItemCount }

        val total = orderedMonkeys[0].inspectedItemCount * orderedMonkeys[1].inspectedItemCount
        println(total)
        return total.toString()
    }

    override fun doB(file: String): String {
        val monkeys = readDoubleLineFile(file).map { parseMonkey(it) }

        // numbers can become too big
        // so because of math magic
        // if we modulo the items by the least common multiple of all test values
        // the number stay small without changing any test outcome
        val allDivisions = monkeys.map { it.test.divisible }
        val divisionLCM = calculateLCM(allDivisions)

        for (i in 0..<10000) {
            doRound(monkeys, false, divisionLCM)
        }

        monkeys.forEach { println("Monkey ${it.id} inspected items ${it.inspectedItemCount} times") }

        val orderedMonkeys = monkeys.sortedByDescending { it.inspectedItemCount }

        val total = orderedMonkeys[0].inspectedItemCount * orderedMonkeys[1].inspectedItemCount
        println(total)
        return total.toString()
    }

    private fun doRound(monkeys: List<Monkey>, performDivision: Boolean, divisionLCM: Long) {
        for (monkey in monkeys) {
            for (item in monkey.items) {
                var newItem = performOperation(item, monkey.operation)

                if (performDivision) {
                    newItem /= 3
                } else {
                    newItem %= divisionLCM
                }

                if (newItem % monkey.test.divisible == 0L) {
                    monkeys[monkey.test.resultTrue].items.add(newItem)
                } else {
                    monkeys[monkey.test.resultFalse].items.add(newItem)
                }

                monkey.inspectedItemCount++
            }
            monkey.items.clear()
        }

    }

    private fun performOperation(startValue: Long, operation: String): Long {
        val parsedOperation = operation.replace("old", startValue.toString())
        val (a, operand, b) = parsedOperation.split(" ")
        return when (operand) {
            "*" -> a.toLong() * b.toLong()
            "+" -> a.toLong() + b.toLong()
            else -> throw IllegalArgumentException("operand $operand is unknown")
        }
    }

    private fun parseMonkey(str: String): Monkey {
        val lines = str.split("\r\n")
        return Monkey(
            id = lines[0].trimIndent().removePrefix("Monkey ").removeSuffix(":").toInt(),
            items = lines[1].trimIndent().removePrefix("Starting items: ").split(", ").map { it.toLong() }.toMutableList(),
            operation = lines[2].trimIndent().removePrefix("Operation: new = "),
            test = Test(
                divisible = lines[3].trimIndent().removePrefix("Test: divisible by ").toLong(),
                resultTrue = lines[4].trimIndent().removePrefix("If true: throw to monkey ").toInt(),
                resultFalse = lines[5].trimIndent().removePrefix("If false: throw to monkey ").toInt(),
            )
        )
    }

    data class Test(
        val divisible: Long,
        val resultTrue: Int,
        val resultFalse: Int
    )

    data class Monkey(
        val id: Int,
        val items: MutableList<Long>,
        val operation: String,
        val test: Test,
        var inspectedItemCount: Long = 0L
    )
}