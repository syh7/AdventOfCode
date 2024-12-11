package syh.year2024.day7

import syh.AbstractAocDay


class Puzzle7 : AbstractAocDay(2024, 7) {
    override fun doA(file: String): String {
        val equations = readSingleLineFile(file)
            .map {
                val (expected, numbers) = it.split(": ")
                expected.toLong() to numbers.split(" ").map { n -> n.toLong() }
            }
        val correctEquations = equations.filter { solveEquationA(it.first, 0, it.second) }
        correctEquations.forEach { println(it) }
        val sum = correctEquations.sumOf { it.first }
        println("total correct equations for B: ${correctEquations.size} for a grand total of $sum")
        return sum.toString()
    }

    override fun doB(file: String): String {
        val equations = readSingleLineFile(file)
            .map {
                val (expected, numbers) = it.split(": ")
                expected.toLong() to numbers.split(" ").map { n -> n.toLong() }
            }
        val correctEquations = equations.filter { solveEquationB(it.first, 0, it.second) }
        correctEquations.forEach { println(it) }
        val sum = correctEquations.sumOf { it.first }
        println("total correct equations for B: ${correctEquations.size} for a grand total of $sum")
        return sum.toString()
    }

    private fun solveEquationA(expectedResult: Long, currentResult: Long, leftoverValues: List<Long>): Boolean {
        if (currentResult > expectedResult) {
            return false
        }
        if (leftoverValues.isEmpty()) {
            return expectedResult == currentResult
        }

        val newNumber = leftoverValues[0]
        return solveEquationA(expectedResult, currentResult * newNumber, leftoverValues.drop(1))
                || solveEquationA(expectedResult, currentResult + newNumber, leftoverValues.drop(1))
    }

    private fun solveEquationB(expectedResult: Long, currentResult: Long, leftoverValues: List<Long>): Boolean {
        if (currentResult > expectedResult) {
            return false
        }
        if (leftoverValues.isEmpty()) {
            return expectedResult == currentResult
        }

        val newNumber = leftoverValues[0]
        return solveEquationB(expectedResult, currentResult * newNumber, leftoverValues.drop(1))
                || solveEquationB(expectedResult, currentResult + newNumber, leftoverValues.drop(1))
                || solveEquationB(expectedResult, currentResult.append(newNumber), leftoverValues.drop(1))
    }

    private fun Long.append(b: Long): Long {
        return (this.toString() + b.toString()).toLong()
    }
}