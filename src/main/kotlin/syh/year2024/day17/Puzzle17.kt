package syh.year2024.day17

import syh.AbstractAocDay
import java.util.*
import kotlin.math.pow


class Puzzle17 : AbstractAocDay(2024, 17) {
    override fun doA(file: String): String {
        val (registerStr, instructionStr) = readDoubleLineFile(file)
        val registers = readRegisters(registerStr)
        val instructions = instructionStr.removePrefix("Program: ").split(",").map { it.toInt() }

        val newInstructions = runProgram(instructions, registers)

        println("returned instructions = $newInstructions")

        return newInstructions.joinToString(",")
    }

    override fun doB(file: String): String {
        val (registerStr, instructionStr) = readDoubleLineFile(file)

        val registers = readRegisters(registerStr)
        val instructions = instructionStr.removePrefix("Program: ").split(",").map { it.toInt() }

        val finalA = findAWithDeque(instructions, registers)

        println("final a value: $finalA")
        return finalA.toString()

    }

    private fun findAWithDeque(instructions: List<Int>, registers: MutableMap<String, Long>): Long {
        val returnValues = mutableSetOf<Long>()
        val deque = ArrayDeque<Pair<Int, Long>>()
        deque.push(instructions.size - 1 to 0L)
        while (deque.isNotEmpty()) {
            val (i, offset) = deque.pop()
            for (a in offset * 8..(offset + 1) * 8) {
                registers["A"] = a
                val newInstructions = runProgram(instructions, registers)
                println("trying a $a returns $newInstructions")
                if (newInstructions == instructions.slice(i..<instructions.size)) {
                    if (i == 0) returnValues.add(a)
                    else deque.add(i - 1 to a)
                }
            }
        }
        println("found ${returnValues.size} correct values: $returnValues")
        return returnValues.min()
    }

    private fun runProgram(instructions: List<Int>, registers: MutableMap<String, Long>): MutableList<Int> {
        var i = 0
        val newInstructions = mutableListOf<Int>()
        while (i in instructions.indices) {
//            println("i = $i")

            val operationCode = instructions[i]
            val operand = instructions[i + 1]

//            println("read operation code $operationCode")
//            println("read operand code $literalOperand")

            when (operationCode) {
                0 -> registers["A"] = (registers["A"]!! / 2.0.pow(getOperand(operand, registers).toInt())).toLong()
                1 -> registers["B"] = registers["B"]!!.xor(operand.toLong())
                2 -> registers["B"] = getOperand(operand, registers) % 8
                3 -> {
                    if (registers["A"] != 0L) {
                        i = operand // set to jnz
                        i -= 2 // prevent jumping at end of cycle
                    }
                }

                4 -> registers["B"] = registers["B"]!!.xor(registers["C"]!!)
                5 -> newInstructions.add((getOperand(operand, registers) % 8).toInt())
                6 -> registers["B"] = (registers["A"]!! / 2.0.pow(getOperand(operand, registers).toInt())).toLong()
                7 -> registers["C"] = (registers["A"]!! / 2.0.pow(getOperand(operand, registers).toInt())).toLong()
            }

            i += 2
        }
//        println("returned instructions = $newInstructions")
        return newInstructions
    }

    private fun readRegisters(registerStr: String): MutableMap<String, Long> {
        val registers = registerStr.split("\r\n")
            .associate {
                val (registerName, registerValue) = it.split(": ")
                registerName.split(" ")[1] to registerValue.toLong()
            }
            .toMutableMap()
        println("read registers")
        println(registers)
        return registers
    }

    private fun getOperand(operandCode: Int, registers: MutableMap<String, Long>): Long {
        return when (operandCode) {
            0 -> 0
            1 -> 1
            2 -> 2
            3 -> 3
            4 -> registers["A"]!!
            5 -> registers["B"]!!
            6 -> registers["C"]!!
            7 -> throw IllegalStateException("Operand code 7 should not appear")
            else -> throw IllegalArgumentException("unknown operand code $operandCode")
        }
    }
}
