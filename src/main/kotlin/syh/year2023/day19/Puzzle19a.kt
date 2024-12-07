package syh.year2023.day19

import syh.readSingleLineFile


fun main() {
    val lines = readSingleLineFile("year2023/day19/actual.txt")

    val (workflows, parts) = readWorkflowsAndParts(lines)

    workflows.forEach { println(it) }
    parts.forEach { println(it) }

    partA(parts, workflows)
}

private fun partA(parts: MutableList<Part>, workflows: MutableList<Workflow>) {
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
