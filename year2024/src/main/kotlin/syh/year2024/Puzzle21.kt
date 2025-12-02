package syh.year2024

import kotlin.math.min
import syh.library.AbstractAocDay

class Puzzle21 : AbstractAocDay(2024, 21) {
    override fun doA(file: String): String {
        val memoise = mutableMapOf<Pair<String, Int>, Long>()

        val codes = readSingleLineFile(file)

        val totalComplexity = codes.sumOf { code ->
            println("deciding code $code")
            val minComplexity = findControlInstructionForKeypad(code).minOf {
                println("control instructions: $it")

                val instructionLength = memoise(it, 1, 2, memoise)
                println("final instruction length is $instructionLength")

                val codeInt = code.removeSuffix("A").toInt()
                val complexity = instructionLength * codeInt
                println("code $code complexity: $instructionLength*$codeInt=$complexity")
                complexity
            }
            println()
            minComplexity
        }
        println("total complexity is $totalComplexity")
        return totalComplexity.toString()
    }

    override fun doB(file: String): String {
        val memoise = mutableMapOf<Pair<String, Int>, Long>()

        val codes = readSingleLineFile(file)

        val totalComplexity = codes.sumOf { code ->
            println("deciding code $code")
            val minComplexity = findControlInstructionForKeypad(code).minOf {
                println("control instructions: $it")

                val instructionLength = memoise(it, 1, 25, memoise)
                println("final instruction length is $instructionLength")

                val codeInt = code.removeSuffix("A").toInt()
                val complexity = instructionLength * codeInt
                println("code $code complexity: $instructionLength*$codeInt=$complexity")
                complexity
            }
            println()
            minComplexity
        }
        println("total complexity is $totalComplexity")
        return totalComplexity.toString()
    }

    private fun memoise(sequence: String, depth: Int, limit: Int, map: MutableMap<Pair<String, Int>, Long>): Long {
        if (map.containsKey(sequence to depth)) {
            return map[sequence to depth]!!
        }

        var length = 0L
        var current = 'A'

        for (next in sequence) {
            val sequenceForChar = getControlpadInstructions(current, next)

            if (depth == limit) {
                // if limit is reached, do not go deeper
                length += sequenceForChar.minOf { it.length }
            } else {
                // limit is not reached, calculate for next robot
                var charMinimum = Long.MAX_VALUE
                sequenceForChar.forEach {
                    val memoise = memoise(it, depth + 1, limit, map)
                    charMinimum = min(memoise, charMinimum)
                }
                length += charMinimum
            }

            current = next
        }
        map[sequence to depth] = length
        return length
    }

    private fun getControlpadInstructions(start: Char, end: Char): List<String> {
        return when (start) {
            'A' -> when (end) {
                'A' -> listOf("A")
                '>' -> listOf("vA")
                '^' -> listOf("<A")
                'v' -> listOf("<vA", "v<A")
                '<' -> listOf("v<<A", "<v<A") // <<v deadspot
                else -> throw IllegalStateException("end $end is not on the controlpad")
            }

            '^' -> when (end) {
                'A' -> listOf(">A")
                '>' -> listOf("v>A", ">vA")
                '^' -> listOf("A")
                'v' -> listOf("vA")
                '<' -> listOf("v<A") // <v deadspot
                else -> throw IllegalStateException("end $end is not on the controlpad")
            }

            '>' -> when (end) {
                'A' -> listOf("^A")
                '>' -> listOf("A")
                '^' -> listOf("<^A", "^<A")
                'v' -> listOf("<A")
                '<' -> listOf("<<A")
                else -> throw IllegalStateException("end $end is not on the controlpad")
            }

            'v' -> when (end) {
                'A' -> listOf(">^A", "^>A")
                '>' -> listOf(">A")
                '^' -> listOf("^A")
                'v' -> listOf("A")
                '<' -> listOf("<A")
                else -> throw IllegalStateException("end $end is not on the controlpad")
            }

            '<' -> when (end) {
                'A' -> listOf(">>^A", ">^>A") // "^>> deadspot"
                '>' -> listOf(">>A")
                '^' -> listOf(">^A") // ^> deadspot
                'v' -> listOf(">A")
                '<' -> listOf("A")
                else -> throw IllegalStateException("end $end is not on the controlpad")
            }

            else -> throw IllegalStateException("start $start is not on the controlpad")
        }

    }

    private fun findControlInstructionForKeypad(code: String): Set<String> {
        // each instruction move has the same size in all possibilities
        // at the end the robot will have to move back to A to press the button
        // thus it does not matter how to move over here
        val firstInstruction = getKeypadInstructions('A', code[0])
        val secondInstruction = getKeypadInstructions(code[0], code[1])
        val thirdInstruction = getKeypadInstructions(code[1], code[2])
        val fourthInstruction = getKeypadInstructions(code[2], 'A')

        return combinations(firstInstruction, secondInstruction, thirdInstruction, fourthInstruction)
        { a: String, b: String, c: String, d: String -> a + b + c + d }
            .toSet()
    }

    private fun <R, T> combinations(list1: List<T>, list2: List<T>, list3: List<T>, list4: List<T>, transform: (T, T, T, T) -> R): Sequence<R> = sequence {
        for (o1 in list1) {
            for (o2 in list2) {
                for (o3 in list3) {
                    for (o4 in list4) {
                        yield(transform(o1, o2, o3, o4))
                    }
                }
            }
        }
    }

    private fun getKeypadInstructions(start: Char, end: Char): List<String> {
        return when (start) {
            'A' -> when (end) {
                'A' -> "".permuteEndingWithA()
                '0' -> "<".permuteEndingWithA()
                '1' -> "<<^".permuteEndingWithA().filterNot { it.contains("<<^") }
                '2' -> "<^".permuteEndingWithA()
                '3' -> "^".permuteEndingWithA()
                '4' -> "<<^^".permuteEndingWithA().filterNot { it.contains("<<^^") }
                '5' -> "<^^".permuteEndingWithA()
                '6' -> "^^".permuteEndingWithA()
                '7' -> "<<^^^".permuteEndingWithA().filterNot { it.contains("<<^^^") }
                '8' -> "<^^^".permuteEndingWithA()
                '9' -> "^^^".permuteEndingWithA()
                else -> throw IllegalStateException("end $end is not on the keypad")
            }

            '0' -> when (end) {
                'A' -> ">".permuteEndingWithA()
                '0' -> "".permuteEndingWithA()
                '1' -> "^<".permuteEndingWithA()
                '2' -> "^".permuteEndingWithA()
                '3' -> ">^".permuteEndingWithA()
                '4' -> "<^^".permuteEndingWithA().filterNot { it.contains("<^^") }
                '5' -> "^^".permuteEndingWithA()
                '6' -> ">^^".permuteEndingWithA()
                '7' -> "^^^<".permuteEndingWithA().filterNot { it.contains("<<^^") }
                '8' -> "^^^".permuteEndingWithA()
                '9' -> "^^^>".permuteEndingWithA()
                else -> throw IllegalStateException("end $end is not on the keypad")
            }

            '1' -> when (end) {
                'A' -> ">>v".permuteEndingWithA().filterNot { it.contains("v>>") }
                '0' -> ">v".permuteEndingWithA().filterNot { it.contains("v>") }
                '1' -> "".permuteEndingWithA()
                '2' -> ">".permuteEndingWithA()
                '3' -> ">>".permuteEndingWithA()
                '4' -> "^".permuteEndingWithA()
                '5' -> ">^".permuteEndingWithA()
                '6' -> ">>^".permuteEndingWithA()
                '7' -> "^^".permuteEndingWithA()
                '8' -> ">^^".permuteEndingWithA()
                '9' -> ">>^^".permuteEndingWithA()
                else -> throw IllegalStateException("end $end is not on the keypad")
            }

            '2' -> when (end) {
                'A' -> ">v".permuteEndingWithA()
                '0' -> "v".permuteEndingWithA()
                '1' -> "<".permuteEndingWithA()
                '2' -> "".permuteEndingWithA()
                '3' -> ">".permuteEndingWithA()
                '4' -> "<^".permuteEndingWithA()
                '5' -> "^".permuteEndingWithA()
                '6' -> ">^".permuteEndingWithA()
                '7' -> "<^^".permuteEndingWithA()
                '8' -> "^^".permuteEndingWithA()
                '9' -> ">^^".permuteEndingWithA()
                else -> throw IllegalStateException("end $end is not on the keypad")
            }

            '3' -> when (end) {
                'A' -> "v".permuteEndingWithA()
                '0' -> "v<".permuteEndingWithA()
                '1' -> "<<".permuteEndingWithA()
                '2' -> "<".permuteEndingWithA()
                '3' -> "".permuteEndingWithA()
                '4' -> "^<<".permuteEndingWithA()
                '5' -> "^<".permuteEndingWithA()
                '6' -> "^".permuteEndingWithA()
                '7' -> "^^<<".permuteEndingWithA()
                '8' -> "^^<".permuteEndingWithA()
                '9' -> "^^".permuteEndingWithA()
                else -> throw IllegalStateException("end $end is not on the keypad")
            }

            '4' -> when (end) {
                'A' -> ">>vv".permuteEndingWithA().filterNot { it.contains("vv>>") }
                '0' -> ">vv".permuteEndingWithA().filterNot { it.contains("vv>") }
                '1' -> "v".permuteEndingWithA()
                '2' -> "v>".permuteEndingWithA()
                '3' -> ">>v".permuteEndingWithA()
                '4' -> "".permuteEndingWithA()
                '5' -> ">".permuteEndingWithA()
                '6' -> ">>".permuteEndingWithA()
                '7' -> "^".permuteEndingWithA()
                '8' -> ">^".permuteEndingWithA()
                '9' -> ">>^".permuteEndingWithA()
                else -> throw IllegalStateException("end $end is not on the keypad")
            }

            '5' -> when (end) {
                'A' -> ">vv".permuteEndingWithA()
                '0' -> "vv".permuteEndingWithA()
                '1' -> "v<".permuteEndingWithA()
                '2' -> "v".permuteEndingWithA()
                '3' -> "v>".permuteEndingWithA()
                '4' -> "<".permuteEndingWithA()
                '5' -> "".permuteEndingWithA()
                '6' -> ">".permuteEndingWithA()
                '7' -> "^<".permuteEndingWithA()
                '8' -> "^".permuteEndingWithA()
                '9' -> "^>".permuteEndingWithA()
                else -> throw IllegalStateException("end $end is not on the keypad")
            }

            '6' -> when (end) {
                'A' -> "vv".permuteEndingWithA()
                '0' -> "<vv".permuteEndingWithA()
                '1' -> "<<v".permuteEndingWithA()
                '2' -> "<v".permuteEndingWithA()
                '3' -> "v".permuteEndingWithA()
                '4' -> "<<".permuteEndingWithA()
                '5' -> "<".permuteEndingWithA()
                '6' -> "".permuteEndingWithA()
                '7' -> "<<^".permuteEndingWithA()
                '8' -> "<^".permuteEndingWithA()
                '9' -> "^".permuteEndingWithA()
                else -> throw IllegalStateException("end $end is not on the keypad")
            }

            '7' -> when (end) {
                'A' -> ">>vvv".permuteEndingWithA().filterNot { it.contains("vvv>>") }
                '0' -> ">vvv".permuteEndingWithA().filterNot { it.contains("vvv>") }
                '1' -> "vv".permuteEndingWithA()
                '2' -> ">vv".permuteEndingWithA()
                '3' -> ">>vv".permuteEndingWithA()
                '4' -> "v".permuteEndingWithA()
                '5' -> ">v".permuteEndingWithA()
                '6' -> ">>v".permuteEndingWithA()
                '7' -> "".permuteEndingWithA()
                '8' -> ">".permuteEndingWithA()
                '9' -> ">>".permuteEndingWithA()
                else -> throw IllegalStateException("end $end is not on the keypad")
            }

            '8' -> when (end) {
                'A' -> "vvvA".permuteEndingWithA()
                '0' -> "vvv".permuteEndingWithA()
                '1' -> "<vv".permuteEndingWithA()
                '2' -> "vv".permuteEndingWithA()
                '3' -> ">vv".permuteEndingWithA()
                '4' -> "<v".permuteEndingWithA()
                '5' -> "v".permuteEndingWithA()
                '6' -> ">v".permuteEndingWithA()
                '7' -> "<".permuteEndingWithA()
                '8' -> "".permuteEndingWithA()
                '9' -> ">".permuteEndingWithA()
                else -> throw IllegalStateException("end $end is not on the keypad")
            }

            '9' -> when (end) {
                'A' -> "vvv".permuteEndingWithA()
                '0' -> "<vvA".permuteEndingWithA()
                '1' -> "<<vA".permuteEndingWithA()
                '2' -> "<vv".permuteEndingWithA()
                '3' -> "vv".permuteEndingWithA()
                '4' -> "<<v".permuteEndingWithA()
                '5' -> "<v".permuteEndingWithA()
                '6' -> "v".permuteEndingWithA()
                '7' -> "<<".permuteEndingWithA()
                '8' -> "<".permuteEndingWithA()
                '9' -> "".permuteEndingWithA()
                else -> throw IllegalStateException("end $end is not on the keypad")
            }

            else -> throw IllegalStateException("start $start is not on the keypad")
        }
    }

    private fun String.permuteEndingWithA(result: String = ""): List<String> {
        val resultList = if (isEmpty()) listOf(result) else flatMapIndexed { i, c -> removeRange(i, i + 1).permute(result + c) }
        return resultList.map { it + "A" }
    }

    private fun String.permute(result: String = ""): List<String> =
        if (isEmpty()) listOf(result) else flatMapIndexed { i, c -> removeRange(i, i + 1).permute(result + c) }
}