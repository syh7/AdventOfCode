package syh.year2022.day13

import syh.readDoubleLineFile
import java.util.*

interface PacketContent
data class ArrayPacket(val content: MutableList<PacketContent> = mutableListOf()) : PacketContent {
    override fun toString(): String {
        return content.toString()
    }
}

data class IntPacket(val content: Int) : PacketContent {
    override fun toString(): String {
        return content.toString()
    }
}

data class Packet(val id: Int, val left: PacketContent, val right: PacketContent)

class PacketComparator {
    fun isCorrectOrder(content1: PacketContent, content2: PacketContent): Boolean {
        val compare = compare(content1, content2)
        return compare == -1
    }

    private fun compare(a: PacketContent, b: PacketContent): Int {
        if (a is IntPacket) {
            if (b is IntPacket) {
                return compare(a, b)
            }
            return compare(a, b as ArrayPacket)
        }
        if (b is IntPacket) {
            return compare(a as ArrayPacket, b)
        }
        return compare(a as ArrayPacket, b as ArrayPacket)
    }

    private fun compare(a: IntPacket, b: IntPacket): Int {
        return a.content.compareTo(b.content)
    }

    private fun compare(a: ArrayPacket, b: IntPacket): Int {
        return compare(a, ArrayPacket(mutableListOf(b)))
    }

    private fun compare(a: IntPacket, b: ArrayPacket): Int {
        return compare(ArrayPacket(mutableListOf(a)), b)
    }

    private fun compare(a: ArrayPacket, b: ArrayPacket): Int {
        val aSize = a.content.size
        val bSize = b.content.size
        var i = 0
        while (i in a.content.indices && i < bSize) {
            val tempCompare = compare(a.content[i], b.content[i])
            if (tempCompare != 0) {
                return tempCompare
            }
            i++
        }
        return aSize.compareTo(bSize)
    }
}

fun main() {
    val comparator = PacketComparator()
    val packets = readDoubleLineFile("year2022/day13/actual.txt")
        .mapIndexed { index, lines ->
            val (first, second) = lines.split("\r\n")
            Packet(id = index + 1, left = parsePacket(first), right = parsePacket(second))
        }

    val totalCorrectOrderPacketIds = packets
        .filter { comparator.isCorrectOrder(it.left, it.right) }
        .sumOf { packet -> packet.id }
    println("total part a: $totalCorrectOrderPacketIds")

    val decoderKey1 = parsePacket("[[2]]")
    val decoderKey2 = parsePacket("[[6]]")
    val packetContents = packets.map { listOf(it.left, it.right) }
        .flatten()
        .toMutableList()
        .apply { add(decoderKey1) }
        .apply { add(decoderKey2) }

    packetContents.sortWith { o1, o2 -> if (comparator.isCorrectOrder(o1, o2)) -1 else 1 }

    packetContents.forEach { println(it) }

    val totalDecoderKey = (packetContents.indexOf(decoderKey1) + 1) * (packetContents.indexOf(decoderKey2) + 1)
    println("totalDecoderKey: $totalDecoderKey")
}

private fun parsePacket(str: String): PacketContent {
    val stack = Stack<PacketContent>()
    stack.push(ArrayPacket()) // add initial for all the others to go into so we do not lose the content on the very last ']'

    var leftoverString = str
    while (leftoverString.isNotEmpty()) {
        when (leftoverString[0]) {
            '[' -> {
                stack.push(ArrayPacket())
                leftoverString = leftoverString.substring(1)
            }

            ']' -> {
                val poppedContent = stack.pop()
                (stack.peek() as ArrayPacket).content.add(poppedContent)
                leftoverString = leftoverString.substring(1)
            }

            ',' -> {
                //ignore this
                leftoverString = leftoverString.substring(1)
            }

            else -> {
                // must be digit
                val builder = StringBuilder()
                var i = 0
                while (leftoverString[i].isDigit()) {
                    builder.append(leftoverString[i])
                    i++
                }
                val intPacket = IntPacket(builder.toString().toInt())
                (stack.peek() as ArrayPacket).content.add(intPacket)
                leftoverString = leftoverString.substring(i)
            }
        }
    }
    return stack.pop()
}
