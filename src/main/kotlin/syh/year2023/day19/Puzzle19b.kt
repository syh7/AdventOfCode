package syh.year2023.day19

import syh.readSingleLineFile


fun main() {
    val lines = readSingleLineFile("year2023/day19/actual.txt")

    val (workflows, _) = syh.year2023.day19.readWorkflowsAndParts(lines)

    val baseConstraints = mutableMapOf<String, syh.year2023.day19.Constraint>()
    baseConstraints["x"] = syh.year2023.day19.Constraint()
    baseConstraints["m"] = syh.year2023.day19.Constraint()
    baseConstraints["a"] = syh.year2023.day19.Constraint()
    baseConstraints["s"] = syh.year2023.day19.Constraint()

    val totalDifferentAcceptedOptions = syh.year2023.day19.partB("in", workflows, baseConstraints)
    println(totalDifferentAcceptedOptions)
}

private fun partB(
    currentWorkflowName: String,
    workflows: List<syh.year2023.day19.Workflow>,
    constraints: MutableMap<String, syh.year2023.day19.Constraint>
): Long {
    var sum = 0L

    val currentWorkflow = workflows.first { it.name == currentWorkflowName }
    for (function in currentWorkflow.functions) {

        if (function.contains(":")) {
            val (f, result) = function.split(":")

            val param = f[0].toString()
            val test = f[1].toString()
            val expectedValue = f.drop(2).toInt()

            val newConstraints = mutableMapOf<String, syh.year2023.day19.Constraint>()
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

            sum += syh.year2023.day19.countValues(result, workflows, newConstraints)

        } else {
            sum += syh.year2023.day19.countValues(function, workflows, constraints)
        }
    }

    return sum
}

private fun countValues(
    currentResult: String,
    workflows: List<syh.year2023.day19.Workflow>,
    constraints: MutableMap<String, syh.year2023.day19.Constraint>
): Long {
    return when (currentResult) {
        "R" -> 0L
        "A" -> constraints.values.map { it.countPossibleValues().toLong() }.reduce { a, b -> a * b }
        else -> syh.year2023.day19.partB(currentResult, workflows, constraints)
    }
}
