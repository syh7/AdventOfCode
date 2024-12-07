package syh.year2023.day19

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

fun readWorkflowsAndParts(lines: List<String>): Pair<MutableList<Workflow>, MutableList<Part>> {
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