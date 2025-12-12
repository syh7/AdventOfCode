package syh.library

import java.util.Arrays
import kotlin.math.abs
import kotlin.math.min


/**
 * Gaussian elimination solver with recursive enumeration for free variables.
 * Solves systems of linear equations over non-negative integers, minimizing the sum of variables.
 */
class GaussianSolver private constructor(private val numVars: Int, private val numConstraints: Int, private val upperBounds: IntArray) {
    // Variable expressions: varCoeffs[v][i] * x[i] + varConstants[v] = x[v]
    private val varCoeffs = Array(numVars) { DoubleArray(numVars) }
    private val varConstants = DoubleArray(numVars)
    private val freeFlags = BooleanArray(numVars)

    init {
        // Initialize each variable as free: x[i] = 1*x[i] + 0
        for (i in 0..<numVars) {
            varCoeffs[i][i] = 1.0
            freeFlags[i] = true
        }
    }

    private fun performElimination(eqCoeffs: Array<DoubleArray>, eqConstants: DoubleArray) {
        for (v in 0..<numVars) {
            eliminateVariable(v, eqCoeffs, eqConstants)
        }
    }

    private fun eliminateVariable(v: Int, eqCoeffs: Array<DoubleArray>, eqConstants: DoubleArray) {
        val pivotRow = findPivotRow(v, eqCoeffs)
        if (pivotRow < 0) {
            return
        }

        val pivot = -eqCoeffs[pivotRow][v]
        extractVariable(v, pivotRow, pivot, eqCoeffs, eqConstants)
        substituteInEquations(v, eqCoeffs, eqConstants)
    }

    private fun findPivotRow(v: Int, eqCoeffs: Array<DoubleArray>): Int {
        for (e in 0..<numConstraints) {
            if (abs(eqCoeffs[e][v]) >= EPS) {
                return e
            }
        }
        return -1
    }

    private fun extractVariable(
        v: Int, e: Int, pivot: Double,
        eqCoeffs: Array<DoubleArray>, eqConstants: DoubleArray
    ) {
        freeFlags[v] = false
        varConstants[v] = eqConstants[e] / pivot
        for (i in 0..<numVars) {
            varCoeffs[v][i] = if ((i == v)) 0.0 else eqCoeffs[e][i] / pivot
        }
    }

    private fun substituteInEquations(v: Int, eqCoeffs: Array<DoubleArray>, eqConstants: DoubleArray) {
        for (j in 0..<numConstraints) {
            val coeff = eqCoeffs[j][v]
            if (abs(coeff) < EPS) {
                continue
            }

            eqCoeffs[j][v] = 0.0
            for (i in 0..<numVars) {
                eqCoeffs[j][i] += coeff * varCoeffs[v][i]
            }
            eqConstants[j] += coeff * varConstants[v]
        }
    }

    private fun findMinimum(): Long {
        val freeVars = collectFreeVariables()
        return enumerate(freeVars, 0, IntArray(numVars), Long.MAX_VALUE)
    }

    private fun collectFreeVariables(): IntArray {
        var freeCount = 0
        for (i in 0..<numVars) {
            if (freeFlags[i]) {
                freeCount++
            }
        }

        val freeVars = IntArray(freeCount)
        var idx = 0
        for (i in 0..<numVars) {
            if (freeFlags[i]) {
                freeVars[idx] = i
                idx++
            }
        }
        return freeVars
    }

    private fun enumerate(freeVars: IntArray, depth: Int, values: IntArray, bestSoFar: Long): Long {
        if (depth == freeVars.size) {
            return evaluate(values)
        }

        val varIdx = freeVars[depth]
        val bound = upperBounds[varIdx]
        var best = bestSoFar

        for (x in 0..bound) {
            values[varIdx] = x
            val result = enumerate(freeVars, depth + 1, values, best)
            if (result < best) {
                best = result
            }
        }
        return best
    }

    private fun evaluate(values: IntArray): Long {
        var total: Long = 0

        // Evaluate variables in reverse order (dependent vars may reference later free vars)
        for (i in numVars - 1 downTo 0) {
            val x = computeVariableValue(i, values)

            // Validate: must be non-negative integer
            if (x < -EPS) {
                return Long.MAX_VALUE
            }
            val rounded = Math.round(x)
            if (abs(x - rounded) > EPS) {
                return Long.MAX_VALUE
            }

            values[i] = rounded.toInt()
            total += rounded
        }
        return total
    }

    private fun computeVariableValue(i: Int, values: IntArray): Double {
        if (freeFlags[i]) {
            return values[i].toDouble()
        }

        var x = varConstants[i]
        val coeffs = varCoeffs[i]
        for (j in 0..<numVars) {
            x += coeffs[j] * values[j]
        }
        return x
    }

    companion object {
        private const val EPS = 1e-8

        fun solve(coefficients: Array<IntArray>, targets: IntArray): Long {
            val numConstraints = targets.size
            if (numConstraints == 0 || coefficients.isEmpty() || coefficients[0].isEmpty()) {
                return if (allZero(targets)) 0 else Long.MAX_VALUE
            }

            val numVars = coefficients[0].size
            val upperBounds = computeUpperBounds(coefficients, targets, numVars)
            val solver = GaussianSolver(numVars, numConstraints, upperBounds)

            // Build equations and perform elimination
            val eqCoeffs = Array(numConstraints) { DoubleArray(numVars) }
            val eqConstants = DoubleArray(numConstraints)

            for (c in 0..<numConstraints) {
                eqConstants[c] = -targets[c].toDouble()
                for (v in 0..<numVars) {
                    eqCoeffs[c][v] = coefficients[c][v].toDouble()
                }
            }

            solver.performElimination(eqCoeffs, eqConstants)
            return solver.findMinimum()
        }

        private fun computeUpperBounds(
            coefficients: Array<IntArray>, targets: IntArray, numVars: Int
        ): IntArray {
            val bounds = IntArray(numVars)
            Arrays.fill(bounds, Int.MAX_VALUE)
            for (c in targets.indices) {
                for (v in 0..<numVars) {
                    if (coefficients[c][v] != 0) {
                        bounds[v] = min(bounds[v].toDouble(), targets[c].toDouble()).toInt()
                    }
                }
            }
            return bounds
        }

        private fun allZero(arr: IntArray): Boolean {
            for (`val` in arr) {
                if (`val` != 0) {
                    return false
                }
            }
            return true
        }
    }
}