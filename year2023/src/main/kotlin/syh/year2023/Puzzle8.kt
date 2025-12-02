package syh.year2023

import syh.library.AbstractAocDay
import syh.library.calculateLCM

class Puzzle8 : AbstractAocDay(2023, 8) {
    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)

        val instructionOrderList = lines[0].split("").filter { it.isNotEmpty() }
        println(instructionOrderList)

        val map = readOptions(lines)
        println(map)

        val endValidation = { option: Option -> option.name == "ZZZ" }

        val stepsA = calculateSteps(map, instructionOrderList, map["AAA"]!!, endValidation)
        println(stepsA)

        return stepsA.toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)

        val instructionOrderList = lines[0].split("").filter { it.isNotEmpty() }
        println(instructionOrderList)

        val map = readOptions(lines)
        println(map)

        val endValidation = { option: Option -> option.name.endsWith("Z") }

        val allStartsB = map.entries.filter { it.key.endsWith("A") }.map { it.value }
        val allEndsB = allStartsB.map { calculateSteps(map, instructionOrderList, it, endValidation) }
        println(allEndsB)
        val lcm = calculateLCM(allEndsB.map { it.toLong() })
        println(lcm)

        return lcm.toString()
    }

    private fun readOptions(lines: List<String>): MutableMap<String, Option> {
        val map = mutableMapOf<String, Option>()
        for (i in 2..<lines.size) {
            println("Line: ${lines[i]}")
            var (name, optionStr) = lines[i].split(" = ")
            optionStr = optionStr.removePrefix("(")
            optionStr = optionStr.removeSuffix(")")
            val (left, right) = optionStr.split(", ")
            map[name] = Option(name, left, right)
        }
        return map
    }

    private fun calculateSteps(
        map: MutableMap<String, Option>,
        instructionOrderList: List<String>,
        start: Option,
        expectedEnd: (Option) -> Boolean,
    ): Int {
        var steps = 0
        var currentOption = start

        while (!expectedEnd(currentOption)) {
            for (instruction in instructionOrderList) {
                currentOption = if (instruction == "L") {
                    map[currentOption.left]!!
                } else {
                    map[currentOption.right]!!
                }
                steps++

                if (expectedEnd(currentOption)) {
                    break
                }
            }
        }
        println("Option ${start.name} took $steps steps and ended in ${currentOption.name}")
        return steps
    }

    data class Option(val name: String, val left: String, val right: String)
}