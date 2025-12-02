package syh.year2022

import java.math.BigDecimal
import syh.library.AbstractAocDay


class Puzzle21 : AbstractAocDay(2022, 21) {

    override fun doA(file: String): String {
        val monkeys = readSingleLineFile(file).associate { l ->
            val (name, res) = l.split(": ")
            name to res
        }

        val resultMap = calculateMonkeyValues(monkeys)
        println("calculated root: ${resultMap["root"]}")
        return resultMap["root"].toString()
    }

    override fun doB(file: String): String {
        val monkeys = readSingleLineFile(file).associate { l ->
            val (name, res) = l.split(": ")
            name to res
        }

        val resultMap = calculateMonkeyValues(monkeys)

        val path = findHumnPath("root", emptyList(), monkeys)
        println("found humn with this path:")
        println(path)

        val (rootLeft, _, rootRight) = monkeys["root"]!!.split(" ")
        val initial = if (rootLeft in path) resultMap[rootRight]!! else resultMap[rootLeft]!!

        val humnValue = path.windowed(2).fold(initial) { acc, (cur, next) ->
            val (l, op, r) = monkeys[cur]!!.split(" ")
            val nextIsRight = r == next
            val other = if (nextIsRight) resultMap[l]!! else resultMap[r]!!

            when (op) {
                "+" -> acc - other
                "-" -> if (nextIsRight) other - acc else acc + other
                "*" -> acc / other
                "/" -> if (nextIsRight) other / acc else acc * other
                else -> throw IllegalArgumentException("unknown operand $op")
            }
//            println("handling window $cur(${monkeys[cur]}) to $next")
//            println("left $l is ${resultMap[l]}, right $r is ${resultMap[r]}")
//            println("acc was $acc, newAcc is $newAcc")
        }
        println("calculated humn: $humnValue")
        return humnValue.toString()
    }

    private fun calculateMonkeyValues(monkeys: Map<String, String>): MutableMap<String, BigDecimal> {
        val resultMap = mutableMapOf<String, BigDecimal>()
        monkeys.filterValues { it.toIntOrNull() != null }.forEach { (k, v) -> resultMap[k] = BigDecimal.valueOf(v.toLong()) }

        while (!resultMap.containsKey("root")) {
            for ((name, rest) in monkeys) {
                if (resultMap.containsKey(name)) {
                    continue
                }
                val (a, op, b) = rest.split(" ")
                if (resultMap.containsKey(a) && resultMap.containsKey(b)) {
                    when (op) {
                        "+" -> resultMap[name] = resultMap[a]!! + resultMap[b]!!
                        "-" -> resultMap[name] = resultMap[a]!! - resultMap[b]!!
                        "*" -> resultMap[name] = resultMap[a]!! * resultMap[b]!!
                        "/" -> resultMap[name] = resultMap[a]!! / resultMap[b]!!
                        else -> throw IllegalArgumentException("unknown operand $op")
                    }
//                    println("calculated monkey $name from $a(${resultMap[a]}) and $b(${resultMap[b]}) with operand $op for total ${resultMap[name]}")
                }
            }
        }
        return resultMap
    }

    private fun findHumnPath(current: String, trail: List<String>, monkeys: Map<String, String>): List<String> {
        if (current == "humn") {
            return trail.drop(1) + "humn"
        }
        if (monkeys[current]!!.toIntOrNull() != null) {
            return emptyList()
        }
        val (a, _, b) = monkeys[current]!!.split(" ")
        return findHumnPath(a, trail + current, monkeys) + findHumnPath(b, trail + current, monkeys)
    }
}
