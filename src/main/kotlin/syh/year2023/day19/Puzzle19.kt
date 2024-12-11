package syh.year2023.day19

import syh.AbstractAocDay

class Puzzle19 : AbstractAocDay(2023, 19) {
    override fun doA(file: String): String {
        val lines = readSingleLineFile(file)

        val (workflows, parts) = readWorkflowsAndParts(lines)

        workflows.forEach { println(it) }
        parts.forEach { println(it) }

        return partA(parts, workflows).toString()
    }

    override fun doB(file: String): String {
        val lines = readSingleLineFile(file)

        val (workflows, _) = readWorkflowsAndParts(lines)

        val baseConstraints = mutableMapOf<String, Constraint>()
        baseConstraints["x"] = Constraint()
        baseConstraints["m"] = Constraint()
        baseConstraints["a"] = Constraint()
        baseConstraints["s"] = Constraint()

        val totalDifferentAcceptedOptions = partB("in", workflows, baseConstraints)
        println(totalDifferentAcceptedOptions)
        return totalDifferentAcceptedOptions.toString()
    }


    private fun partA(parts: MutableList<Part>, workflows: MutableList<Workflow>): Long {
        val totalSum = parts.sumOf { part ->
            if (isPartAccepted(part, workflows)) {
                println("part $part is accepted")
                part.getTotalValue()
            } else {
                println("part $part is rejected")
                0
            }
        }

        println("total sum = $totalSum")
        return totalSum.toLong()
    }

    private fun isPartAccepted(part: Part, workflows: List<Workflow>): Boolean {
        val endStates = listOf("R", "A")
        var nextWorkflow = "in"

        while (nextWorkflow !in endStates) {
            val workflow = workflows.first { it.name == nextWorkflow }
            nextWorkflow = getResultFromWorkflow(part, workflow)
        }

        return nextWorkflow == "A"
    }

    private fun getResultFromWorkflow(part: Part, workflow: Workflow): String {
        for (i in workflow.functions.indices) {
            val currentFunction = workflow.functions[i]
            if (currentFunction.contains(":")) {
                val (f, result) = currentFunction.split(":")
                val param = f[0].toString()
                val test = f[1].toString()
                val expectedValue = f.drop(2).toInt()
                if (paramPassesTest(part.getParam(param), test, expectedValue)) {
                    return result
                }
            } else {
                return currentFunction
            }
        }
        throw IllegalStateException("part $part did not pass any workflow")
    }

    private fun paramPassesTest(actual: Int, test: String, expected: Int): Boolean {
        return when (test) {
            ">" -> actual > expected
            "<" -> actual < expected
            else -> throw IllegalArgumentException("unexpected test string $test")
        }
    }

    private fun partB(
        currentWorkflowName: String,
        workflows: List<Workflow>,
        constraints: MutableMap<String, Constraint>
    ): Long {
        var sum = 0L

        val currentWorkflow = workflows.first { it.name == currentWorkflowName }
        for (function in currentWorkflow.functions) {

            if (function.contains(":")) {
                val (f, result) = function.split(":")

                val param = f[0].toString()
                val test = f[1].toString()
                val expectedValue = f.drop(2).toInt()

                val newConstraints = mutableMapOf<String, Constraint>()
                for (pair in constraints) {
                    newConstraints[pair.key] = pair.value
                }

                if (test == ">") {
                    newConstraints[param] = newConstraints[param]!!.newMoreThan(expectedValue)
                    constraints[param] = constraints[param]!!.newLessThan(expectedValue + 1)
                }
                if (test == "<") {
                    newConstraints[param] = newConstraints[param]!!.newLessThan(expectedValue)
                    constraints[param] = constraints[param]!!.newMoreThan(expectedValue - 1)
                }

                sum += countValues(result, workflows, newConstraints)

            } else {
                sum += countValues(function, workflows, constraints)
            }
        }

        return sum
    }

    private fun countValues(
        currentResult: String,
        workflows: List<Workflow>,
        constraints: MutableMap<String, Constraint>
    ): Long {
        return when (currentResult) {
            "R" -> 0L
            "A" -> constraints.values.map { it.countPossibleValues().toLong() }.reduce { a, b -> a * b }
            else -> partB(currentResult, workflows, constraints)
        }
    }

    data class Part(
        val x: Int, val m: Int, val a: Int, val s: Int
    ) {
        fun getParam(param: String): Int {
            if (param == "x") return x
            if (param == "m") return m
            if (param == "a") return a
            if (param == "s") return s
            throw IllegalArgumentException("unexpected param $param")
        }

        fun getTotalValue(): Int {
            return x + m + a + s
        }
    }

    data class Constraint(val moreThan: Int = 0, val lessThan: Int = 4001) {
        fun newMoreThan(otherMoreThan: Int): Constraint {
            return Constraint(moreThan.coerceAtLeast(otherMoreThan), lessThan)
        }

        fun newLessThan(otherLessThan: Int): Constraint {
            return Constraint(moreThan, lessThan.coerceAtMost(otherLessThan))
        }

        fun countPossibleValues(): Int {
            return if (moreThan > lessThan) 0 else lessThan - moreThan - 1
        }
    }

    data class Workflow(
        val name: String, val functions: List<String>
    )

    private fun readWorkflowsAndParts(lines: List<String>): Pair<MutableList<Workflow>, MutableList<Part>> {
        val workflows = mutableListOf<Workflow>()
        val parts = mutableListOf<Part>()

        var i = 0
        while (lines[i].isNotEmpty()) {
            val line = lines[i]
            val (name, rest) = line.split("{")
            val functions = rest.removeSuffix("}").split(",")
            workflows.add(Workflow(name, functions))
            i++
        }

        // skip empty line
        i++

        while (i in lines.indices) {
            val params = lines[i].removePrefix("{").removeSuffix("}").split(",")
            val values = params.map { it.split("=")[1].toInt() }
            parts.add(Part(values[0], values[1], values[2], values[3]))
            i++
        }
        return Pair(workflows, parts)
    }
}
