package syh.year2024.day24

import syh.AbstractAocDay


class Puzzle24 : AbstractAocDay(2024, 24) {
    override fun doA(file: String): String {
        val (values, operations) = readValueAndOperations(file)

        val operationKeys = operations.keys.toMutableSet()

        while (operationKeys.isNotEmpty()) {
            val keysToRemove = mutableSetOf<String>()
            for (key in operationKeys) {
                val currentOperation = operations[key]!!
                if (values.containsKey(currentOperation.inputA) && values.containsKey(currentOperation.inputB)) {
                    doOperation(currentOperation, values)
                    keysToRemove.add(key)
                }
            }
            if (keysToRemove.isEmpty()) {
                println("could not find any more keys to remove")
                break
            }
            operationKeys.removeAll(keysToRemove)
        }

//        println("final value map:")
//        values.toSortedMap().forEach { println(it) }

        val relevantValues = values.filter { (k, _) -> k.startsWith("z") }

//        println("relevant values:")
//        relevantValues.toSortedMap().forEach { println(it) }

        val finalBitNumber = relevantValues.toSortedMap().reversed().map { (_, v) -> v }.joinToString("")
        val decimal = finalBitNumber.toLong(2)
        println("read decimal $decimal from bits $finalBitNumber")

        return decimal.toString()
    }

    override fun doB(file: String): String {
        val (_, operations) = readValueAndOperations(file)
        val gates = operations.values.toSet()
        val maxBit = operations.keys.filter { it.startsWith("z") }.map { it.removePrefix("z") }.maxBy { it.toInt() }
        val finalOutput = "z$maxBit"
        println("final bit is $finalOutput")


        // it should be a ripple-carry adder
        // bit N is calculated by adding An AND Bn XOR Zn-1

        // define gate types as
        // A    XOR B    -> VAL0     <= FAGate0
        // A    AND B    -> VAL1     <= FAGate1
        // VAL0 AND CIN  -> VAL2     <= FAGate2
        // VAL0 XOR CIN  -> SUM      <= FAGate3
        // VAL1 OR  VAL2 -> COUT     <= FAGate4

        // FAGate0 gates should not end in Z except for x00 and y00
        // FAGate3 gates that are indirect (not an x or y) should output to z
        // all output gates should be FAGate3
        // all FAGate0 should output to a FAGate3

        val flags = mutableSetOf<String>()

        val faGates0 = gates.filter { it.isDirect() }.filter { it.operation == "XOR" }
        for (gate in faGates0) {
            val isDirectFirst = gate.inputA == "x00" || gate.inputB == "x00"
            if (isDirectFirst) {
                if (gate.output != "z00") {
                    // direct first input should be direct first output
                    flags.add(gate.output)
                }
                continue

            }
            if (gate.output == "z00") {
                flags.add(gate.output)
            }

            // FAGate0 should not be output gates
            if (gate.isOutput()) {
                flags.add(gate.output)
            }
        }

        val faGates3 = gates.filter { it.operation == "XOR" }.filterNot { it.isDirect() }
        // all FAGate3 gates should be output
        faGates3.filterNot { it.isOutput() }.forEach { flags.add(it.output) }

        // all output gates should be XOR
        // expect the last bit output gate, that should be an OR
        val outputGates = gates.filter { it.isOutput() }
        for (gate in outputGates) {
            if (gate.output == finalOutput) {
                if (gate.operation != "OR") {
                    flags.add(gate.output)
                }
            } else {
                if (gate.operation != "XOR") {
                    flags.add(gate.output)
                }
            }
        }

        // all FAgate0 gates should output to a FAGate3
        val checkNext = mutableSetOf<Operation>()
        for (gate in faGates0) {
            if (gate.output == "z00" || gate.output in flags) {
                continue
            }
            val noMatch = faGates3.none { it.inputA == gate.output || it.inputB == gate.output }
            if (noMatch) {
                flags.add(gate.output)
                checkNext.add(gate)
            }
        }

        // find the correct value for the flagged input
        for (gate in checkNext) {
            val intendedOutput = "z" + gate.inputA[1] + gate.inputA[2]
            val match = faGates3.first { it.output == intendedOutput } // should only be 1
            val inputs = setOf(match.inputA, match.inputB)
            val orMatch = gates.filter { it.operation == "OR" }.first { it.output in inputs } // should only be 1
            val correctOutput = inputs.first { it != orMatch.output }
            flags.add(correctOutput)
        }
        val allFlags = flags.sorted().joinToString(",")
        println("all found flags: $allFlags")
        return allFlags
    }

    private fun readValueAndOperations(file: String): Pair<MutableMap<String, Int>, Map<String, Operation>> {
        val (start, lines) = readDoubleLineFile(file)

        val values = start.split("\r\n").associate {
            val (name, value) = it.split(": ")
            name to value.toInt()
        }.toMutableMap()

        val operations = lines.split("\r\n").associate { str ->
            val (input, end) = str.split(" -> ")
            val (i1, operation, i2) = input.split(" ")
            end to Operation(output = end, inputA = i1, inputB = i2, operation = operation)
        }
        return values to operations
    }

    private fun doOperation(operation: Operation, values: MutableMap<String, Int>) {
        val a = values[operation.inputA]!!
        val b = values[operation.inputB]!!
        val value = when (operation.operation) {
            "AND" -> a.and(b)
            "OR" -> a.or(b)
            "XOR" -> a.xor(b)
            else -> throw IllegalArgumentException("Unsupported operation $operation")
        }
        values[operation.output] = value
    }

    data class Operation(val output: String, val inputA: String, val inputB: String, val operation: String) {
        fun isDirect(): Boolean {
            return inputA.startsWith("x") || inputB.startsWith("x")
        }

        fun isOutput(): Boolean {
            return output.startsWith("z")
        }
    }
}
