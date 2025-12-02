package syh.year2022

import java.util.Stack
import syh.library.AbstractAocDay

sealed interface PacketContent : Comparable<PacketContent> {
    data class IntPacket(val content: Int) : PacketContent {
        override fun compareTo(other: PacketContent): Int {
            return if (other is IntPacket) {
                this.content.compareTo(other.content)
            } else {
                ArrayPacket(mutableListOf(this)).compareTo(other)
            }
        }

        override fun toString(): String {
            return content.toString()
        }
    }

    data class ArrayPacket(val content: MutableList<PacketContent> = mutableListOf()) : PacketContent {
        override fun compareTo(other: PacketContent): Int {
            if (other is IntPacket) {
                return this.compareTo(ArrayPacket(mutableListOf(other)))
            }
            val aSize = this.content.size
            val bSize = (other as ArrayPacket).content.size
            var i = 0
            while (i in this.content.indices && i < bSize) {
                val tempCompare = this.content[i].compareTo(other.content[i])
                if (tempCompare != 0) {
                    return tempCompare
                }
                i++
            }
            return aSize.compareTo(bSize)
        }

        override fun toString(): String {
            return content.toString()
        }
    }

}

data class Packet(val id: Int, val left: PacketContent, val right: PacketContent)


class Puzzle13 : AbstractAocDay(2022, 13) {
    override fun doA(file: String): String {
        val packets = readDoubleLineFile(file)
            .mapIndexed { index, lines ->
                val (first, second) = lines.split("\r\n")
                Packet(id = index + 1, left = parsePacket(first), right = parsePacket(second))
            }

        val totalCorrectOrderPacketIds = packets
            .filter { isCorrectOrder(it.left, it.right) }
            .sumOf { packet -> packet.id }
        println("total part a: $totalCorrectOrderPacketIds")
        return totalCorrectOrderPacketIds.toString()
    }

    override fun doB(file: String): String {
        val packets = readDoubleLineFile(file)
            .mapIndexed { index, lines ->
                val (first, second) = lines.split("\r\n")
                Packet(id = index + 1, left = parsePacket(first), right = parsePacket(second))
            }

        val decoderKey1 = parsePacket("[[2]]")
        val decoderKey2 = parsePacket("[[6]]")
        val packetContents = packets.map { listOf(it.left, it.right) }
            .flatten()
            .toMutableList()
            .apply { add(decoderKey1) }
            .apply { add(decoderKey2) }

        packetContents.sortWith { o1, o2 -> if (isCorrectOrder(o1, o2)) -1 else 1 }

        packetContents.forEach { println(it) }

        val totalDecoderKey = (packetContents.indexOf(decoderKey1) + 1) * (packetContents.indexOf(decoderKey2) + 1)
        println("totalDecoderKey: $totalDecoderKey")
        return totalDecoderKey.toString()
    }

    private fun isCorrectOrder(content1: PacketContent, content2: PacketContent): Boolean {
        val compare = content1.compareTo(content2)
        return compare == -1
    }

    private fun parsePacket(str: String): PacketContent {
        val stack = Stack<PacketContent>()
        stack.push(PacketContent.ArrayPacket()) // add initial for all the others to go into so we do not lose the content on the very last ']'

        var leftoverString = str
        while (leftoverString.isNotEmpty()) {
            when (leftoverString[0]) {
                '[' -> {
                    stack.push(PacketContent.ArrayPacket())
                    leftoverString = leftoverString.substring(1)
                }

                ']' -> {
                    val poppedContent = stack.pop()
                    (stack.peek() as PacketContent.ArrayPacket).content.add(poppedContent)
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
                    val intPacket = PacketContent.IntPacket(builder.toString().toInt())
                    (stack.peek() as PacketContent.ArrayPacket).content.add(intPacket)
                    leftoverString = leftoverString.substring(i)
                }
            }
        }
        return stack.pop()
    }
}